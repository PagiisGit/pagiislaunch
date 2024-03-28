package pagiisnet.pagiisnet;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import pagiisnet.pagiisnet.R;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LinkView extends AppCompatActivity {


    private WebView webviewLinks;

    private String shareItemId;

    private androidx.appcompat.widget.Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_view);


        /*mToolbar = findViewById(R.id.appBarLayout);

        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle("Links");*/

        webviewLinks = findViewById(R.id.webview);

        WebSettings webSettings = webviewLinks.getSettings();

        webSettings.setJavaScriptEnabled(true);


        shareItemId = getIntent().getExtras().get("share_item_id").toString();


        webviewLinks.setWebViewClient(new Callback());

        webviewLinks.loadUrl(shareItemId);

        //webviewLinks = new WebView(getApplicationContext());
        //setContentView(webviewLinks);
        //WebViewClient myWebViewClient = new WebViewClient();
        //webviewLinks.setWebViewClient(myWebViewClient);


        if (shareItemId != null && !shareItemId.isEmpty()) {

        } else {
            Toast.makeText(this, "Link not found", Toast.LENGTH_SHORT).show();
        }

        //checkIfUrlPresent();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.linkview_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //This button is for the explicit content view
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_add)
        {
            Intent intent = new Intent(LinkView.this, LinkPost.class);
            intent.putExtra("share_item_id", "null");
            intent.putExtra("share_item_id_key", "null");
            startActivity(intent);
        } else {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    static class Callback extends WebViewClient {

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        }
    }
}