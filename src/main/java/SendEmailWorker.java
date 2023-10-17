import java.awt.Desktop;
import java.net.URI;
import java.util.logging.Logger;

import org.camunda.bpm.client.ExternalTaskClient;
public class SendEmailWorker {
    private final static Logger LOGGER = Logger.getLogger(SendEmailWorker.class.getName());

    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                .asyncResponseTimeout(5000) // long polling timeout
                .build();

        // subscribe to an external task topic as specified in the process
        client.subscribe("sendEmail")
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler((externalTask, externalTaskService) -> {
                    // Put your business logic here

                    // Get a process variable
                    String email = externalTask.getVariable("userEmail");
                    String status = externalTask.getVariable("complaintDepartment");
                    String complaint = externalTask.getVariable("userComplaint");
                    String complaintMessage = externalTask.getVariable("complaintMessage");

                    LOGGER.info("\nEmail: " + email + "\nComplaint: " + complaint + "\nStatus: " + status + "\nMessage: " + complaintMessage);

                    // Complete the task
                    externalTaskService.complete(externalTask);
                })
                .open();
    }
}
