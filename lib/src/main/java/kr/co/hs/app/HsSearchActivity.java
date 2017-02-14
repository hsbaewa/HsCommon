package kr.co.hs.app;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import kr.co.hs.R;

/**
 * 생성된 시간 2017-02-14, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsCommon
 * 패키지명 : kr.co.hs.app
 */

public abstract class HsSearchActivity extends HsActivity implements SearchView.OnQueryTextListener{
    private SearchView searchViewAndroidActionBar;

    protected abstract int getSearchViewHintColor();
    protected abstract String getSearchViewHint();

    @Override
    public boolean onQueryTextChange(String newText) {
        searchViewAndroidActionBar.clearFocus();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu_searchview, menu);
        final MenuItem searchViewItem = menu.findItem(R.id.Common_Menu_Search);
        searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setMaxWidth(Integer.MAX_VALUE);
        searchViewAndroidActionBar.setQueryHint(getSearchViewHint());
        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchViewAndroidActionBar.findViewById(R.id.search_src_text);
        if(searchAutoComplete != null){
            searchAutoComplete.setHintTextColor(getSearchViewHintColor());
        }
        searchViewAndroidActionBar.setOnQueryTextListener(this);
        ImageView imageViewClose = (ImageView) searchViewAndroidActionBar.findViewById(R.id.search_close_btn);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Find EditText view
                EditText et = (EditText) findViewById(R.id.search_src_text);

                //Clear the text from EditText view
                if(et != null)
                    et.setText("");

                //Clear query
                searchViewAndroidActionBar.setQuery("", false);
                //Collapse the action view
                searchViewAndroidActionBar.onActionViewCollapsed();
                //Collapse the search widget
//                searchViewAndroidActionBar.collapseActionView();
                MenuItemCompat.collapseActionView(searchViewItem);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
