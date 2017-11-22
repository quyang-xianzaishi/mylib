package com.example.administrator.lubanone.adapter.homepage;

import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.utils.Node;
import com.example.administrator.lubanone.utils.UserPopupWindow;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by HQOCSHheqing on 2016/8/3.
 *
 * @description 适配器类，就是listview最常见的适配器写法
 */
public class HomeMyTeamTreeAdapterNew extends BaseAdapter implements OnClickListener{

    //大家经常用ArrayList，但是这里为什么要使用LinkedList
    // ，后面大家会发现因为这个list会随着用户展开、收缩某一项而频繁的进行增加、删除元素操作，
    // 因为ArrayList是数组实现的，频繁的增删性能低下，而LinkedList是链表实现的，对于频繁的增删
    //操作性能要比ArrayList好。
    private LinkedList<Node> nodeLinkedList;
    private List chooseList;
    private LayoutInflater inflater;
    private int retract;//缩进值
    private Context mContext;
    private View parent;

    public HomeMyTeamTreeAdapterNew(Context context, ListView listView, LinkedList<Node> linkedList){
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        nodeLinkedList = linkedList;
        chooseList = new ArrayList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expandOrCollapse(position);
            }
        });
        //缩进值，大家可以将它配置在资源文件中，从而实现适配
        retract = (int)(context.getResources().getDisplayMetrics().density*10+0.5f);
    }

    public HomeMyTeamTreeAdapterNew(Context context, ListView listView, LinkedList<Node> linkedList,View parent){
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.parent = parent;
        nodeLinkedList = linkedList;
        chooseList = new ArrayList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expandOrCollapse(position);
            }
        });
        //缩进值，大家可以将它配置在资源文件中，从而实现适配
        retract = (int)(context.getResources().getDisplayMetrics().density*10+0.5f);
    }

    /**
     * 展开或收缩用户点击的条目
     * @param position
     */
    private void expandOrCollapse(int position){
        Node node = nodeLinkedList.get(position);
        if (node != null && !node.isLeaf()){
            boolean old = node.isExpand();
            if (old){
                List<Node> nodeList = node.get_childrenList();
                int size = nodeList.size();
                Node tmp = null;
                for (int i = 0;i < size;i++){
                    tmp = nodeList.get(i);
                    if (tmp.isExpand()){
                        collapse(tmp,position+1);
                    }
                    nodeLinkedList.remove(position+1);
                }
            }else{
                nodeLinkedList.addAll(position + 1, node.get_childrenList());
            }
            node.setIsExpand(!old);
            notifyDataSetChanged();
        }
    }
    /**
     * 递归收缩用户点击的条目
     * 因为此中实现思路是：当用户展开某一条时，就将该条对应的所有子节点加入到nodeLinkedList
     * ，同时控制缩进，当用户收缩某一条时，就将该条所对应的子节点全部删除，而当用户跨级缩进时
     * ，就需要递归缩进其所有的孩子节点，这样才能保持整个nodeLinkedList的正确性，同时这种实
     * 现方式避免了每次对所有数据进行处理然后插入到一个list，最后显示出来，当数据量一大，就会卡顿，
     * 所以这种只改变局部数据的方式性能大大提高。
     * @param position
     */
    private void collapse(Node node,int position){
        node.setIsExpand(false);
        List<Node> nodes = node.get_childrenList();
        int size = nodes.size();
        Node tmp = null;
        for (int i = 0;i < size;i++){
            tmp = nodes.get(i);
            if (tmp.isExpand()){
                collapse(tmp,position+1);
            }
            nodeLinkedList.remove(position+1);
        }
    }

    @Override
    public int getCount() {
        return nodeLinkedList.size();
    }

    @Override
    public Object getItem(int position) {
        return nodeLinkedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.my_team_listview_item,null);
            holder = new ViewHolder();
            holder.imageView = (ImageView)convertView.findViewById(R.id.id_treenode_icon);
            holder.label = (TextView)convertView.findViewById(R.id.id_treenode_label);
            holder.stateLinear = (LinearLayout)convertView.findViewById(R.id.my_team_member_state_linear);
            holder.state = (TextView) convertView.findViewById(R.id.my_team_member_state_tx);
            holder.stateImg = (TextView) convertView.findViewById(R.id.my_team_member_state_img);
            holder.teamNumber = (TextView) convertView.findViewById(R.id.my_team_number);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        final Node node = nodeLinkedList.get(position);
        holder.label.setText(node.get_label());
        if(node.get_icon() == -1){
            //holder.imageView.setVisibility(View.INVISIBLE);
            holder.imageView.setVisibility(View.GONE);
        }else{
            //holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
            holder.imageView.setImageResource(node.get_icon());
        }
        if(node.get_state()!=null&&node.get_state().contains("封号")){
          holder.stateImg.setBackground(mContext.getResources().getDrawable(R.mipmap.seal_off));
            holder.stateImg.setVisibility(View.VISIBLE);
            holder.state.setTextColor(mContext.getResources().getColor(R.color._FF0000));
        }else if(node.get_state()!=null&&node.get_state().contains("冻结")){
          holder.stateImg.setBackground(mContext.getResources().getDrawable(R.mipmap.freeze));
            holder.stateImg.setVisibility(View.VISIBLE);
            holder.state.setTextColor(mContext.getResources().getColor(R.color._FF9800));
        }else{
            holder.stateImg.setVisibility(View.GONE);
        }
        holder.state.setText(node.get_state());
        holder.teamNumber.setText(node.get_team_size()+"人");
        holder.state.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击查看原因
                showReason( holder.state.getText().toString(),node.get_reason());
            }
        });
        convertView.setPadding(node.get_level()*retract,20,5,20);
        return convertView;
    }

    static class ViewHolder{
        public ImageView imageView;
        public TextView label;
        public LinearLayout stateLinear;
        public TextView state;
        public TextView stateImg;
        public TextView teamNumber;

    }

    private void showReason(String state,String reason){
        WindowManager wm = ((Activity)mContext).getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final UserPopupWindow userPopupWindow = new UserPopupWindow((Activity) mContext,
            mContext.getResources().getLayout(R.layout.check_state_reason_dialog),width/3*2);
        TextView textView1 = (TextView) userPopupWindow.getView().findViewById(R.id.member_state_reason_hint);
        TextView textView2 = (TextView) userPopupWindow.getView().findViewById(R.id.member_state_reason);
        TextView textView3 = (TextView) userPopupWindow.getView().findViewById(R.id.close);
        if(state!=null&&state.contains("封号")){
            textView1.setText("封号原因：");
        }else if(state!=null&&state.contains("冻结")){
            textView1.setText("冻结原因：");
        }
        textView2.setText(reason);
        textView3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                userPopupWindow.dismiss();
            }
        });
        userPopupWindow.showUserPopupWindow(parent);
        userPopupWindow.setOnClickListener(userPopupWindow.getView(), new OnClickListener() {
            @Override
            public void onClick(View v) {
                userPopupWindow.dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}
