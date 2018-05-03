import javafx.scene.control.Alert;
import org.apache.axis2.AxisFault;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.doszhan.SOAPServiceStub;
import com.doszhan.SOAPServiceStub.AddArticle;
import com.doszhan.SOAPServiceStub.FetchArticleList;
import com.doszhan.SOAPServiceStub.DelArticle;
import com.doszhan.SOAPServiceStub.UpdArticle;
import com.doszhan.SOAPServiceStub.FetchArticleListResponse;
import com.doszhan.SOAPServiceStub.Article;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;


public class Client {
    private static final Logger log = LogManager.getLogger(Client.class);
    private SOAPServiceStub serviceStub = null;

    public void connect(String addr) {
        try {
            serviceStub = new SOAPServiceStub(addr);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    public ArrayList<Article> getArticle() {
        FetchArticleList operation = new FetchArticleList();
        FetchArticleListResponse response;
        Article[] articlesArray = new Article[0];

        try {
            response = serviceStub.fetchArticleList(operation);
            articlesArray = response.get_return();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (articlesArray == null){
            return new ArrayList<>();
        }else {
            return new ArrayList<>(Arrays.asList(articlesArray));
        }

    }

    public void addArticle(Article article){
        AddArticle operation = new AddArticle();
        operation.setArticle(article);

        try {
            boolean response = serviceStub.addArticle(operation).get_return();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void removeArticle(Article article){
        DelArticle operation = new DelArticle();
        operation.setArticle(article);

        try {
            boolean reponse = serviceStub.delArticle(operation).get_return();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void editArticle(Article article){
        UpdArticle operation = new UpdArticle();
        operation.setSomeArticle(article);

        try {
            boolean response = serviceStub.updArticle(operation).get_return();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }





}
