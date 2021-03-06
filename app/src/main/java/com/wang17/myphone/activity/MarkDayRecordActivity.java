package com.wang17.myphone.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang17.myphone.R;
import com.wang17.myphone.fragment.ActionBarFragment;
import com.wang17.myphone.model.DateTime;
import com.wang17.myphone.database.DayItem;
import com.wang17.myphone.database.MarkDay;
import com.wang17.myphone.database.Setting;
import com.wang17.myphone.dao.DataContext;
import com.wang17.myphone.util._Session;
import com.wang17.myphone.util._Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class MarkDayRecordActivity extends AppCompatActivity implements ActionBarFragment.OnActionFragmentBackListener {

    // 视图变量
    ConstraintLayout root;
    // 类变量
    private DataContext dataContext;
    private List<MarkDay> markDays;
    private List<ListItemData> listItemDatas;
    private SexualDayListdAdapter recordListdAdapter;
//    private Switch aSwitch;
    public DayItem dayItem;
    // 值变量
    private int max;
    private UUID itemId;

    /*

    20 4
    21 4.4
    22 4.8
    23 5.2
    24 5.6
    25 6
    26 6.4
    27 6.8
    28 7.2
    29 7.6
    30 8

    31 8.8
    32 9.6
    33 10.4
    34 11.2
    35 12
    36 12.8
    37 13.6
    38 14.4
    39 15.2
    40 16

    41 16.5
    42 17
    43 17.5
    44 18
    45 18.5
    46 19
    47 19.5
    48 20
    49 20.5
    50 21

    51 21.9
    52 22.8
    53 23.7
    54 24.6
    55 25.5
    56 26.4
    57 27.3
    58 28.2
    59 29.1
    60 30

    20 4
    30 8
    40 16
    50 21
    60 30
    年二十者四日一泄;年三十者，
    八日一泄;年四十者，
    十六日一泄;
    年五十者，
    二十一日一泄;
    年六十者，毕，闭精勿复泄也。
    若体力犹壮者，一月一泄。
    凡人气力，自相有强盛过人，亦不可抑忍。
    久而不泄，至生痈疽。
    若年过六十，而有数旬不得交接，意中平平者，可闭精勿泄也。
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_mark_day_record);

//            aSwitch = (Switch) findViewById(R.id.switch1);
//            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    fillListItemData(isChecked, dataContext.getSetting(Setting.KEYS.is_mark_day_show_all, "false").getBoolean());
//                    recordListdAdapter.notifyDataSetChanged();
//                }
//            });

            dataContext = new DataContext(this);
            root = (ConstraintLayout) findViewById(R.id.activity_sexual_day_record);
            itemId = UUID.fromString(getIntent().getStringExtra("id"));

            dayItem = dataContext.getDayItem(itemId);


            listItemDatas = new ArrayList<>();

            fillListItemData(false, dataContext.getSetting(Setting.KEYS.is显示所有MarkDay, "false").getBoolean());

            if (dayItem != null) {
                if (dayItem.getId() == _Session.UUID_NULL) {
                    max = _Utils.getTargetInHours(_Session.BIRTHDAY);
                } else {
                    max = dayItem.getTargetInHour();
                }
            }

            ListView listView_sexualDays = (ListView) findViewById(R.id.lv_markday);
            listView_sexualDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (view.findViewById(R.id.layout_operate).getVisibility() == View.VISIBLE) {
                        view.findViewById(R.id.layout_operate).setVisibility(View.INVISIBLE);
                    } else {
                        for (int i = 0; i < parent.getChildCount(); i++) {
                            parent.getChildAt(i).findViewById(R.id.layout_operate).setVisibility(View.INVISIBLE);
                        }
                        view.findViewById(R.id.layout_operate).setVisibility(View.VISIBLE);
                    }
                }
            });

            recordListdAdapter = new SexualDayListdAdapter();
            listView_sexualDays.setAdapter(recordListdAdapter);
        } catch (Exception e) {
            _Utils.printException(this, e);
        }
    }

    private void fillListItemData(boolean isSortByInterval, boolean isShowAll) {


        markDays = dataContext.getMarkDays(itemId, true);
        listItemDatas.clear();

        for (int position = 0; position < markDays.size(); position++) {
            ListItemData lid = new ListItemData();

            MarkDay markDay = markDays.get(position);
            if (!isShowAll && (markDay.getSummary() != null && markDay.getSummary().equals("hide")))
                continue;
            DateTime nextDateTime = new DateTime();
            if (position > 0) {
                nextDateTime = markDays.get(position - 1).getDateTime();
            } else {
                lid.isCurrent = true;
            }
            lid.Interval = nextDateTime.getTimeInMillis() - markDay.getDateTime().getTimeInMillis();
            lid.isDayModel = false;
            if (dayItem.getSummary() != null) {
                switch (dayItem.getSummary()) {
                    case "天":
                        lid.Interval = DateTime.dayOffset(markDay.getDateTime(), nextDateTime) * 24;
                        lid.isDayModel = true;
                        break;
                    case "当天":
                        lid.Interval = (DateTime.dayOffset(markDay.getDateTime(), nextDateTime) + 1) * 24;
                        lid.isDayModel = true;
                        break;
                }
            }
            lid.Date = markDays.get(position).getDateTime();
            lid.MarkDay = markDay;

            listItemDatas.add(lid);
            if (isSortByInterval)
                Collections.sort(listItemDatas, new SortByInterval());
        }
    }

    class SortByInterval implements Comparator {
        public int compare(Object o1, Object o2) {
            ListItemData s1 = (ListItemData) o1;
            ListItemData s2 = (ListItemData) o2;
            return s1.Interval <= s2.Interval ? 1 : -1;
        }
    }

    @Override
    public void onBackButtonClickListener() {
        this.finish();
    }


    protected class SexualDayListdAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return listItemDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final int index = position;
            try {
                convertView = View.inflate(MarkDayRecordActivity.this, R.layout.inflate_list_item_mark_day_record, null);
                final ListItemData listItemData = listItemDatas.get(position);
//                final MarkDay sexualDay = markDays.get(position);
//                DateTime nextDateTime = new DateTime();
//                if (position > 0) {
//                    nextDateTime = markDays.get(position - 1).getDateTime();
//                }
//                long havePassedInHour = nextDateTime.getTimeInMillis() - sexualDay.getDateTime().getTimeInMillis();

                final RelativeLayout layoutRoot =  convertView.findViewById(R.id.layout_root);
                TextView textViewStartDay =  convertView.findViewById(R.id.textView_startDay);
                TextView textViewStartTime =  convertView.findViewById(R.id.textView_startTime);
                TextView textViewInterval = convertView.findViewById(R.id.textView_interval);
                final LinearLayout layoutOperate = convertView.findViewById(R.id.layout_operate);
                FrameLayout layoutEdit = convertView.findViewById(R.id.layout_edit);
                final FrameLayout layoutDel = convertView.findViewById(R.id.layout_del);
                ProgressBar progressBar = convertView.findViewById(R.id.progressBar);
                progressBar.setMax(max);
                layoutRoot.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        String[] menu = new String[]{"编辑", "删除"};

                        new android.support.v7.app.AlertDialog.Builder(MarkDayRecordActivity.this).setItems(menu, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        showEditSexualDayDialog(listItemData.MarkDay);
                                        break;
                                    case 1:
                                        try {
                                            new AlertDialog.Builder(MarkDayRecordActivity.this).setTitle("删除确认").setMessage("是否要删除当前记录?").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dataContext.deleteMarkDay(markDays.get(index).getId());
                                                    fillListItemData(false, dataContext.getSetting(Setting.KEYS.is显示所有MarkDay, "false").getBoolean());
                                                    recordListdAdapter.notifyDataSetChanged();
                                                    dialog.cancel();
                                                    snackbar("删除成功");
                                                }
                                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            }).show();
                                        } catch (Exception e) {
                                            _Utils.printException(MarkDayRecordActivity.this, e);
                                        }
                                        break;
                                }
                            }
                        }).show();
                        return true;
                    }
                });
                layoutEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                layoutDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                DateTime date = listItemData.Date;
                textViewStartDay.setText(date.getMonthStr() + "月" + date.getDayStr() + "日");
                textViewStartTime.setText(date.getHour() + ":" + date.getMiniteStr());
                textViewInterval.setText(DateTime.toSpanString(listItemData.Interval,4,3));
                progressBar.setProgress((int) (listItemData.Interval / 3600000));
                Log.e("wangsc",listItemData.isDayModel+"");
                if (listItemData.isDayModel) {
                    textViewInterval.setText(listItemData.Interval / 24 + "天");
                    progressBar.setProgress((int) listItemData.Interval);
                }
//                if (aSwitch.isChecked() && listItemData.isCurrent) {
//                    textViewInterval.setTextColor(getResources().getColor(R.color.colorAccent, null));
//                    textViewStartDay.setTextColor(getResources().getColor(R.color.colorAccent, null));
//                    textViewStartTime.setTextColor(getResources().getColor(R.color.colorAccent, null));
//                }
            } catch (Exception e) {
                _Utils.printException(MarkDayRecordActivity.this, e);
            }
            return convertView;
        }
    }

    class ListItemData {
        public ListItemData() {
            this.isCurrent = false;
        }

        public boolean isCurrent;
        public MarkDay MarkDay;
        public DateTime Date;
        public long Interval;
        public boolean isDayModel;
    }

    public void showEditSexualDayDialog(MarkDay markDay) {

        final MarkDay sd = markDay;
        View view = View.inflate(MarkDayRecordActivity.this, R.layout.inflate_dialog_date_picker, null);
        AlertDialog dialog = new AlertDialog.Builder(MarkDayRecordActivity.this).setView(view).create();
        dialog.setTitle("设定时间");

        DateTime dateTime = markDay.getDateTime();
        final int year = dateTime.getYear();
        int month = dateTime.getMonth();
//        int maxDay = dateTime.getActualMaximum(Calendar.DAY_OF_MONTH);
        int day = dateTime.getDay();
        int hour = dateTime.getHour();

        String[] yearNumbers = new String[3];
        for (int i = year - 2; i <= year; i++) {
            yearNumbers[i - year + 2] = i + "年";
        }
        String[] monthNumbers = new String[12];
        for (int i = 0; i < 12; i++) {
            monthNumbers[i] = i + 1 + "月";
        }
        String[] dayNumbers = new String[31];
        for (int i = 0; i < 31; i++) {
            dayNumbers[i] = i + 1 + "日";
        }
        String[] hourNumbers = new String[24];
        for (int i = 0; i < 24; i++) {
            hourNumbers[i] = i + "点";
        }
        final NumberPicker npYear = view.findViewById(R.id.np_target_tap);
        final NumberPicker npMonth = view.findViewById(R.id.npMonth);
        final NumberPicker npDay = view.findViewById(R.id.npDay);
        final NumberPicker npHour = view.findViewById(R.id.npHour);
        npYear.setMinValue(year - 2);
        npYear.setMaxValue(year);
        npYear.setValue(year);
        npYear.setDisplayedValues(yearNumbers);
        npYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); // 禁止对话框打开后数字选择框被选中
        npMonth.setMinValue(1);
        npMonth.setMaxValue(12);
        npMonth.setDisplayedValues(monthNumbers);
        npMonth.setValue(month + 1);
        npMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); // 禁止对话框打开后数字选择框被选中
        npDay.setMinValue(1);
        npDay.setMaxValue(31);
        npDay.setDisplayedValues(dayNumbers);
        npDay.setValue(day);
        npDay.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); // 禁止对话框打开后数字选择框被选中
        npHour.setMinValue(0);
        npHour.setMaxValue(23);
        npHour.setDisplayedValues(hourNumbers);
        npHour.setValue(hour);
        npHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); // 禁止对话框打开后数字选择框被选中


        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int year = npYear.getValue();
                    int month = npMonth.getValue() - 1;
                    int day = npDay.getValue();
                    int hour = npHour.getValue();
                    DateTime selectedDateTime = new DateTime(year, month, day, hour, 0, 0);
                    sd.setDateTime(selectedDateTime);
                    dataContext.editMarkDay(sd);
                    recordListdAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                } catch (Exception e) {
                    _Utils.printException(MarkDayRecordActivity.this, e);
                }
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    _Utils.printException(MarkDayRecordActivity.this, e);
                }
            }
        });
        dialog.show();
    }


    private void snackbar(String message) {
        try {
            Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("wangsc", e.getMessage());
        }
    }
}
