package protocol;

import service.MicrocontrollerService;
import utils.Location;

public class MessageHandler {
   private MicrocontrollerService microcontrollerService;

   public MessageHandler(MicrocontrollerService microcontrollerService) {
      this.microcontrollerService = microcontrollerService;
   }

   public void handleMessage(Message message, boolean isLog) {
      // descompress the message content
      String decompressedContent = message.getContent();

      String[] parts = decompressedContent.split(" | ");
      String operation = parts[0];

      switch (operation) {
         case "CREATE_MC":
            // Handle create microcontroller
            String name = parts[1];
            String location = parts[2];
            String ipAddress = parts[3];
            try {
               microcontrollerService.addMicrocontroller(name, Location.fromString(location), ipAddress, isLog);
            } catch (Exception e) {
               System.out.println("Error adding microcontroller: " + e.getMessage());
            }
            break;
         case "CREATE_CR":
            // Handle create climate record
            break;
         default:
            break;
      }
   }
}
