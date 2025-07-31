package com.training.query.Training.Query.collections;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
//import com.training.query.Training.Query.util.EmailUtil;


@Document(collection = "MailServer")
@Data
public class MailServer {




    @Id
    private String id;
    private String host;
    private int port;
    private String username;
    private String password;
    private String protocol;
    private String fromEmail;
    private String fromName;
    private boolean useAuth;
    private boolean useTLS;
    private Boolean enabled;


}

