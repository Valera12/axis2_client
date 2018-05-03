import com.doszhan.SOAPServiceStub.Article;

import java.util.ArrayList;



public class Controller {

        private Client client;

        Controller(){
            this.client = new Client();
        }

        public boolean connect(String host){
            client.connect(host);
            return true;
        }

        public void add(Article article){
            client.addArticle(article);
        }

        public void remove(Article article){
            client.removeArticle(article);
        }

        public void edit(Article article){
            client.editArticle(article);
        }

        public ArrayList<Article> getArticle(){
            return client.getArticle();
        }


}
