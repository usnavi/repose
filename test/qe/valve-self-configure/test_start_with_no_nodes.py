#!/usr/bin/env python

import repose
import requests
import unittest
import conf
import pathutil
import xmlrunner as _xmlrunner
import logging
import time
import argparse
import os

logger = logging.getLogger(__name__)


target_hostname = '50.57.189.15'
target_port = 8080
config_dir = pathutil.join(os.getcwd(), 'etc/repose')
deploy_dir = pathutil.join(os.getcwd(), 'var/repose')
artifact_dir = pathutil.join(os.getcwd(), 'usr/share/repose/filters')
log_file = pathutil.join(os.getcwd(), 'var/log/repose/current.log')
stop_port = 7777


def setUpModule():
    # Set up folder hierarchy and install repose JAR/EARs if needed
    logger.debug('setUpModule')
    pathutil.create_folder(config_dir)
    pathutil.create_folder(deploy_dir)
    pathutil.create_folder(os.path.dirname(log_file))
    pass


def get_status_code_from_url(url, timeout=None):
    logger.debug('get_status_code_from_url(url="%s")' % url)
    return requests.get(url, timeout=timeout, verify=False).status_code


def apply_config_set(config_set_name, params=None):
    if params is None:
        params = {}
    conf.process_config_set(config_set_name, verbose=False,
                            destination_path=config_dir, params=params)


class TestStartWithSingleNonLocalhostNode(unittest.TestCase):
    def setUp(self):
        logger.debug('setUp')

        self.port = 11111

        pathutil.clear_folder(config_dir)
        params = {
            'target_hostname': target_hostname,
            'target_port': target_port,
            'host' : 'example.com',
            'port' : self.port,
            'deploy_dir': deploy_dir,
            'artifact_dir': artifact_dir,
            'log_file': log_file,
        }
        apply_config_set('valve-self-common', params=params)
        apply_config_set('container-no-port', params=params)
        apply_config_set('one-node', params=params)
        self.repose = repose.ReposeValve(config_dir=config_dir,
                                         stop_port=stop_port)
        time.sleep(20)

    def tearDown(self):
        logger.debug('tearDown')
        if self.repose is not None:
            self.repose.stop()
            time.sleep(5)

    def make_request_and_assert_status_code(self, url, expected_status_code):
        logger.debug('asserting status code - url = %s' % url)
        logger.debug('expected status code = %i' % expected_status_code)
        status_code = get_status_code_from_url(url)
        logger.debug('received status code = %i' % status_code)
        self.assertEqual(expected_status_code, status_code)

    def make_request_and_assert_connection_fails(self, url):
        logger.debug('asserting connection fails - url = %s' % url)
        self.assertRaises(requests.ConnectionError, get_status_code_from_url,
                          url)

    def runTest(self):
        logger.debug('runTest')

        #raw_input('Ready and waiting.')

        url = 'http://localhost:%i/' % (self.port)
        self.make_request_and_assert_connection_fails(url)

        # change the hostname to 'localhost'
        params = {
            'target_hostname': target_hostname,
            'target_port': target_port,
            'host' : 'localhost',
            'port' : self.port,
        }
        apply_config_set('one-node', params=params)
        time.sleep(20)

        url = 'http://localhost:%i/' % (self.port)
        self.make_request_and_assert_status_code(url, 200)


class TestStartWithZeroNodes(unittest.TestCase):
    def setUp(self):
        logger.debug('setUp')

        self.port = 11111

        pathutil.clear_folder(config_dir)
        params = {
            'target_hostname': target_hostname,
            'target_port': target_port,
            'deploy_dir': deploy_dir,
            'artifact_dir': artifact_dir,
            'log_file': log_file,
        }
        apply_config_set('valve-self-common', params=params)
        apply_config_set('container-no-port', params=params)
        apply_config_set('zero-nodes', params=params)
        self.repose = repose.ReposeValve(config_dir=config_dir,
                                         stop_port=stop_port)
        time.sleep(20)

    def tearDown(self):
        logger.debug('tearDown')
        if self.repose is not None:
            self.repose.stop()
            time.sleep(5)

    def make_request_and_assert_status_code(self, url, expected_status_code):
        logger.debug('asserting status code - url = %s' % url)
        logger.debug('expected status code = %i' % expected_status_code)
        status_code = get_status_code_from_url(url)
        logger.debug('received status code = %i' % status_code)
        self.assertEqual(expected_status_code, status_code)

    def make_request_and_assert_connection_fails(self, url):
        logger.debug('asserting connection fails - url = %s' % url)
        self.assertRaises(requests.ConnectionError, get_status_code_from_url,
                          url)

    def runTest(self):
        logger.debug('runTest')

        #raw_input('Ready and waiting.')

        url = 'http://localhost:%i/' % (self.port)
        self.make_request_and_assert_connection_fails(url)

        # add a node for localhost
        params = {
            'target_hostname': target_hostname,
            'target_port': target_port,
            'host' : 'localhost',
            'port' : self.port,
        }
        apply_config_set('one-node', params=params)
        time.sleep(20)

        url = 'http://localhost:%i/' % (self.port)
        self.make_request_and_assert_status_code(url, 200)


def run():

    parser = argparse.ArgumentParser()
    parser.add_argument('--print-log', help="Print the log to STDERR.",
                        action='store_true')
    args = parser.parse_args()

    if args.print_log:
        logging.basicConfig(level=logging.DEBUG)

    logger.debug('run')
    setUpModule()

    suite = unittest.TestSuite()

    loader = unittest.TestLoader()
    load_tests = loader.loadTestsFromTestCase
    suite.addTest(load_tests(TestStartWithSingleNonLocalhostNode))
    suite.addTest(load_tests(TestStartWithZeroNodes))

    testRunner = _xmlrunner.XMLTestRunner(output='test-reports')

    result = testRunner.run(suite)


if __name__ == '__main__':
    run()