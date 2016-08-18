package ai.harmony.ravel.primitives;

/**
 * Created by lauril on 8/18/16.
 */
public class CheckType {
     public static Type getType(int tokenType) {
          switch ( tokenType ) {
               case CymbolParser.K_VOID :  return Symbol.Type.tVOID;
               case CymbolParser.K_INT :   return Symbol.Type.tINT;
               case CymbolParser.K_FLOAT : return Symbol.Type.tFLOAT;
          }
          return Symbol.Type.tINVALID;
     }
