package com.example.administrator.lubanone.adapter.homepage;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.home.ActivateActivity;
import com.example.administrator.lubanone.activity.home.BuySeedsMemberInfoActivity;
import com.example.administrator.lubanone.activity.home.MyTeamActivityNew;
import com.example.administrator.lubanone.bean.training.MyTeamResultBean;
import com.example.administrator.lubanone.interfaces.OnItemViewListener;
import com.example.administrator.lubanone.utils.Node;
import com.example.administrator.lubanone.utils.UserPopupWindow;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.DebugUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by HQOCSHheqing on 2016/8/3.
 *
 * @description 适配器类，就是listview最常见的适配器写法
 */
public class HomeMyTeamTreeAdapterTwo extends BaseAdapter implements OnClickListener {


  public static final int TYPE_TITLE = 0;

  public static final int TYPE_COMPANY = 1;

  private Object buySellBean;


  //大家经常用ArrayList，但是这里为什么要使用LinkedList
  // ，后面大家会发现因为这个list会随着用户展开、收缩某一项而频繁的进行增加、删除元素操作，
  // 因为ArrayList是数组实现的，频繁的增删性能低下，而LinkedList是链表实现的，对于频繁的增删
  //操作性能要比ArrayList好。
  private LinkedList<Node> nodeLinkedList;
  private List chooseList;
  private LayoutInflater inflater;
  private int retract;//缩进值
  private MyTeamActivityNew mContext;
  private View parent;
  private MyTeamResultBean resultBean;
  private ListView mListView;
  private int mHeight;
  private OnItemViewListener mListener;
  private int mHeight1;

  public HomeMyTeamTreeAdapterTwo(MyTeamResultBean resultBean, MyTeamActivityNew context,
      ListView listView,
      LinkedList<Node> linkedList) {
    inflater = LayoutInflater.from(context);
    this.mContext = context;
    nodeLinkedList = linkedList;
    chooseList = new ArrayList();
    this.resultBean = resultBean;
    this.mListView = listView;
    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        expandOrCollapse(position);
      }
    });
    //缩进值，大家可以将它配置在资源文件中，从而实现适配
    retract = (int) (context.getResources().getDisplayMetrics().density * 10 + 0.5f);
  }

  public HomeMyTeamTreeAdapterTwo(MyTeamResultBean myTeamResultBean, MyTeamActivityNew context,
      ListView listView, LinkedList<Node> linkedList,
      View parent, OnItemViewListener listener) {
    inflater = LayoutInflater.from(context);
    this.mContext = (MyTeamActivityNew) context;
    this.parent = parent;
    nodeLinkedList = linkedList;
    chooseList = new ArrayList();
    this.resultBean = myTeamResultBean;
    this.mListView = listView;
    this.mListener = listener;
    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        expandOrCollapse(position);
      }
    });
    //缩进值，大家可以将它配置在资源文件中，从而实现适配
    retract = (int) (context.getResources().getDisplayMetrics().density * 10 + 0.5f);
  }

  /**
   * 展开或收缩用户点击的条目
   */
  private void expandOrCollapse(int position) {
    if (position == 0) {
      return;
    }
    if (CollectionUtils.isEmpty(nodeLinkedList)) {
      return;
    }

    position = position - 1;

    Node node = nodeLinkedList
        .get(nodeLinkedList.size() == position ? nodeLinkedList.size() : position);

    if (node != null && !node.isLeaf()) {

      System.out.println("HomeMyTeamTreeAdapterTwo.expandOrCollapse 3=true");

      boolean old = node.isExpand();
      System.out.println("HomeMyTeamTreeAdapterTwo.expandOrCollapse expand=" + old);

      if (old) {
        List<Node> nodeList = node.get_childrenList();
        int size = nodeList.size();
        Node tmp = null;
        for (int i = 0; i < size; i++) {
          tmp = nodeList.get(i);
          if (tmp.isExpand()) {
            collapse(tmp, position + 1);
          }

          if (null != tmp && !tmp.isLeaf()) {
            boolean expand = tmp.isExpand();

            if (expand) {
              List childrenList = tmp.get_childrenList();
              int sizeChild = childrenList.size();

              Node tmp2 = null;
              for (int a = 0; a < sizeChild; a++) {
                tmp2 = nodeList.get(i);
                if (tmp2.isExpand()) {
                  collapse(tmp2, position + 1);
                }
                nodeList.remove(a);
              }

            }
          }

          nodeLinkedList.remove(position + 1);

        }
      } else {
        //节点位置
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
   */
  private void collapse(Node node, int position) {
    node.setIsExpand(false);
    List<Node> nodes = node.get_childrenList();
    int size = nodes.size();
    Node tmp = null;
    for (int i = 0; i < size; i++) {
      tmp = nodes.get(i);
      if (tmp.isExpand()) {
        collapse(tmp, position + 1);
      }
      nodeLinkedList.remove(position + 1);
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (0 == position) {
      return TYPE_TITLE;
    } else {
      return TYPE_COMPANY;
    }
  }

  @Override
  public int getViewTypeCount() {
    return 2;
  }

  @Override
  public int getCount() {
    if (CollectionUtils.isEmpty(nodeLinkedList)) {
      return 1;
    }
    return nodeLinkedList.size() + 1;
  }

  @Override
  public Object getItem(int position) {
    if (0 == position) {
      return buySellBean;
    } else {
      return nodeLinkedList.get(position - 1);
    }
  }
//
//  @Override
//  public int getCount() {
//    return nodeLinkedList.size();
//  }
//
//  @Override
//  public Object getItem(int position) {
//    return nodeLinkedList.get(position);
//  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
  @Override
  public View getView(int position, View convertView, final ViewGroup parent) {
    final ViewHolder holder;
    final TitleViewHolder titleHolder;

    int type = getItemViewType(position);

    switch (type) {
      case TYPE_TITLE:
        if (null == convertView) {
          convertView = LayoutInflater.from(mContext).inflate(R.layout.my_team_head, null);

          mHeight1 = convertView.getHeight();

          titleHolder = new TitleViewHolder();

          titleHolder.sensitize_member = (ImageView) convertView
              .findViewById(R.id.sensitize_member);
          titleHolder.my_leader = (TextView) convertView.findViewById(R.id.my_leader);
          titleHolder.tv_my_level = (TextView) convertView.findViewById(R.id.tv_my_level);
          titleHolder.tv_persons = (TextView) convertView.findViewById(R.id.tv_persons);
          titleHolder.tv_look_new = (TextView) convertView.findViewById(R.id.tv_look_new);
          titleHolder.tv_look = (ImageView) convertView.findViewById(R.id.tv_look);
          titleHolder.rl_container = (RelativeLayout) convertView.findViewById(R.id.rl_container);

          convertView.setTag(titleHolder);
        } else {
          titleHolder = (TitleViewHolder) convertView.getTag();
        }

        if (resultBean != null) {
          titleHolder.sensitize_member.setOnClickListener(new MyOnClickListener(0, null));
          titleHolder.my_leader.setOnClickListener(new MyOnClickListener(1, null));
          titleHolder.tv_look.setOnClickListener(new MyOnClickListener(2, titleHolder.tv_look));
          titleHolder.tv_look_new.setOnClickListener(new MyOnClickListener(2, titleHolder.tv_look));
          titleHolder.rl_container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              mListener.onItem(null, 0, mHeight1, titleHolder.rl_container);
              expandOrCollapse(1);
            }
          });

          titleHolder.my_leader.setText(resultBean.getLeader());
          titleHolder.tv_my_level.setText(resultBean.getLevel());
          titleHolder.tv_persons.setText(resultBean.getTeamsize());
        }

        if (!mContext.getIconShow()) {
          titleHolder.tv_look.setBackgroundResource(R.mipmap.down_2x_gray);
        } else {
          titleHolder.tv_look.setBackgroundResource(R.mipmap.icon_up);
        }

        break;

      case TYPE_COMPANY:

        if (convertView == null) {
          convertView = inflater.inflate(R.layout.my_team_listview_item, null);
          holder = new ViewHolder();
          holder.imageView = (ImageView) convertView.findViewById(R.id.id_treenode_icon);
          holder.label = (TextView) convertView.findViewById(R.id.id_treenode_label);
          holder.stateLinear = (LinearLayout) convertView
              .findViewById(R.id.my_team_member_state_linear);
          holder.state = (TextView) convertView.findViewById(R.id.my_team_member_state_tx);
          holder.stateImg = (TextView) convertView.findViewById(R.id.my_team_member_state_img);
          holder.teamNumber = (TextView) convertView.findViewById(R.id.my_team_number);
          convertView.setTag(holder);
        } else {
          holder = (ViewHolder) convertView.getTag();
        }
        final Node node = nodeLinkedList.get(position - 1);
        holder.label.setText(node.get_label());
        if (node.get_icon() == -1) {
          //holder.imageView.setVisibility(View.INVISIBLE);
          holder.imageView.setVisibility(View.GONE);
        } else {
          //holder.imageView.setVisibility(View.VISIBLE);
          holder.imageView.setVisibility(View.GONE);
          holder.imageView.setImageResource(node.get_icon());
        }
        if (node.get_state() != null && node.get_state().contains("封号")) {
          holder.stateImg.setBackground(mContext.getResources().getDrawable(R.mipmap.seal_off));
          holder.stateImg.setVisibility(View.VISIBLE);
          holder.state.setTextColor(mContext.getResources().getColor(R.color._FF0000));
        } else if (node.get_state() != null && node.get_state().contains("冻结")) {
          holder.stateImg.setBackground(mContext.getResources().getDrawable(R.mipmap.freeze));
          holder.stateImg.setVisibility(View.VISIBLE);
          holder.state.setTextColor(mContext.getResources().getColor(R.color._FF9800));
        } else {
          holder.stateImg.setVisibility(View.GONE);
        }
        holder.state.setText(node.get_state());
        holder.teamNumber.setText(isZeor(node.get_team_size()) ? "" : node.get_team_size() + "人");
        holder.state.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            //点击查看原因
            showReason(holder.state.getText().toString(), node.get_reason());
          }
        });
        convertView.setPadding(node.get_level() * retract, 20, 5, 20);

        break;
    }

    return convertView;
  }

  private boolean isZeor(String team_size) {
    if (null == team_size || "".equals(team_size) || "0".equals(team_size)) {
      return true;
    }
    return false;
  }

  static class ViewHolder {

    public ImageView imageView;
    public TextView label;
    public LinearLayout stateLinear;
    public TextView state;
    public TextView stateImg;
    public TextView teamNumber;

  }

  private static class TitleViewHolder {

    ImageView sensitize_member;
    TextView my_leader;
    TextView tv_my_level;
    TextView tv_persons;
    ImageView tv_look;
    TextView tv_look_new;
    RelativeLayout rl_container;

  }

  private void showReason(String state, String reason) {
    WindowManager wm = ((Activity) mContext).getWindowManager();
    int width = wm.getDefaultDisplay().getWidth();
    int height = wm.getDefaultDisplay().getHeight();
    final UserPopupWindow userPopupWindow = new UserPopupWindow((Activity) mContext,
        mContext.getResources().getLayout(R.layout.check_state_reason_dialog), width / 3 * 2);
    TextView textView1 = (TextView) userPopupWindow.getView()
        .findViewById(R.id.member_state_reason_hint);
    TextView textView2 = (TextView) userPopupWindow.getView()
        .findViewById(R.id.member_state_reason);
    TextView textView3 = (TextView) userPopupWindow.getView().findViewById(R.id.close);
    if (state != null && state.contains("封号")) {
      textView1.setText("封号原因：");
    } else if (state != null && state.contains("冻结")) {
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

  private class MyOnClickListener implements OnClickListener {


    private int i;
    private ImageView mImageView;

    public MyOnClickListener(int i, ImageView view) {
      this.i = i;
      this.mImageView = view;
    }

    @Override
    public void onClick(View v) {
      switch (i) {
        case 0://激活会员
          Intent intent = new Intent();
          intent.setClass(mContext, ActivateActivity.class);
          mContext.startActivity(intent);
          break;
        case 1://我的领导
          Intent intent2 = new Intent();
          intent2.setClass(mContext, BuySeedsMemberInfoActivity.class);
          intent2.putExtra("userId", DebugUtils.convert(resultBean.getLeader(), ""));
          mContext.startActivity(intent2);
          break;
        case 2://查看会员
          mListener.onItem(null, 0, mHeight1, mImageView);
          expandOrCollapse(1);
          break;
      }

    }
  }
}
