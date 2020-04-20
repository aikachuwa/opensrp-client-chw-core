package org.smartregister.chw.core.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Menu;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

import org.smartregister.chw.core.R;
import org.smartregister.chw.core.adapter.CoreStockUsageItemDetailsAdapter;
import org.smartregister.chw.core.dao.StockUsageReportDao;
import org.smartregister.chw.core.model.StockUsageItemDetailsModel;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.core.utils.StockUsageReportUtils;
import org.smartregister.view.activity.SecuredActivity;
import org.smartregister.view.customcontrols.CustomFontTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CoreStockInventoryItemDetailsReportActivity extends SecuredActivity {
    protected AppBarLayout appBarLayout;

    private String evaluateStockName(String stockName) {
        String stock_name;
        switch (stockName) {
            case ("Male Condoms"):
                stock_name = "Male condom";
                break;
            case ("Female Condoms"):
                stock_name = "Female condom";
                break;
            case ("Cycle beads (Standard day method)"):
                stock_name = "Standard day method";
                break;
            case ("Paracetamol"):
                stock_name = "Panadol";
                break;
            default:
                stock_name = stockName;
                break;
        }
        return stock_name;
    }

    private List<StockUsageItemDetailsModel> stockUsageItemDetailsModelList(String stockName, String providerName) {
        StockUsageReportUtils stockUsageReportUtils = new StockUsageReportUtils();
        String stockMonth;
        String stockYear;
        String stockUsage;
        List<StockUsageItemDetailsModel> stockUsageItemDetailsModelList = new ArrayList<>();
        if (stockUsageReportUtils.getPreviousMonths().size() > 0) {
            for (Map.Entry<Integer, Integer> entry : stockUsageReportUtils.getPreviousMonths().entrySet()) {
                stockMonth = stockUsageReportUtils.monthConverter(entry.getKey());
                stockYear = entry.getValue().toString();
                String monthNo = stockUsageReportUtils.getMonthNumber(stockMonth.substring(0, 3));
                String stock_name = evaluateStockName(stockName);
                stockUsage = providerName.equalsIgnoreCase(this.getString(R.string.all_chw)) ? StockUsageReportDao.getAllStockUsageForMonth(monthNo, stock_name, stockYear) : StockUsageReportDao.getStockUsageForMonth(monthNo, stock_name, stockYear, providerName);
                stockUsageItemDetailsModelList.add(new StockUsageItemDetailsModel(stockMonth, stockYear, stockUsage));
            }
        }
        return stockUsageItemDetailsModelList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    protected void onCreation() {
        setContentView(R.layout.activity_stock_usage_item_details);
        Intent intent = getIntent();
        String stockName = intent.getStringExtra(CoreConstants.HfStockUsageUtil.STOCK_NAME);
        String providerName = intent.getStringExtra(CoreConstants.HfStockUsageUtil.PROVIDER_NAME);


        TextView textViewName = findViewById(R.id.item_detail_name);
        textViewName.setText(String.format(this.getString(R.string.stock_used_text), stockName));

        RecyclerView recyclerView = findViewById(R.id.rv_stock_usage_item_detail_report);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        CoreStockUsageItemDetailsAdapter coreStockUsageItemDetailsAdapter = new CoreStockUsageItemDetailsAdapter(stockUsageItemDetailsModelList(stockName, providerName));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(coreStockUsageItemDetailsAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        Toolbar toolbar = findViewById(R.id.back_stock_usage_toolbar);
        CustomFontTextView toolBarTextView = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            upArrow.setVisible(true, true);
            actionBar.setHomeAsUpIndicator(upArrow);
            actionBar.setElevation(0);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        toolBarTextView.setOnClickListener(v -> finish());
        appBarLayout = findViewById(R.id.app_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.setOutlineProvider(null);
        }
    }

    @Override
    protected void onResumption() {
        //Implements Method From super Class
    }
}