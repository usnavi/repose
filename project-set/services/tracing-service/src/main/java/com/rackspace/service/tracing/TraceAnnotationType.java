/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.service.tracing;

public enum TraceAnnotationType {

   BOOL(0),
   BYTES(1),
   I16(2),
   I32(3),
   I64(4),
   DOUBLE(5),
   STRING(6);
   private final int value;

   private TraceAnnotationType(int value) {
      this.value = value;
   }

   /**
    * Get the integer value of this enum value, as defined in the Thrift IDL.
    */
   public int getValue() {
      return value;
   }
   
   public static TraceAnnotationType findByValue(int value) { 
    switch (value) {
      case 0:
        return BOOL;
      case 1:
        return BYTES;
      case 2:
        return I16;
      case 3:
        return I32;
      case 4:
        return I64;
      case 5:
        return DOUBLE;
      case 6:
        return STRING;
      default:
        return null;
    }
  }
}
