package utils;

public enum Location {
   NATAL,
   FORTALEZA,
   RECIFE,
   SALVADOR,
   TERESINA,
   SAO_LUIS,
   MACEIO,
   JOAO_PESSOA,
   ARACAJU,
   MOSSORO;

   public static Location fromString(String location) {
      try {
         return Location.valueOf(location.toUpperCase());
      } catch (IllegalArgumentException e) {
         return null; 
      }
   }
}
