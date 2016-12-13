package models;

import io.netty.util.internal.StringUtil;

import javax.persistence.*;

@Entity
public class App {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public String id;
    public String deepLink;

    public App() {

    }

    public App(String id, String link) {
        this.id = id;
        this.deepLink = link;
    }

    @Override
    public String toString() {
        if(StringUtil.isNullOrEmpty(id) || StringUtil.isNullOrEmpty(deepLink))
            return "";
        else
            return id + "," + deepLink + "\n";
    }
}
