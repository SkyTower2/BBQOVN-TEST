package com.me.ml.bluetooth_kit.search;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * SearchTask 类是一个在 Android 应用中用来执行搜索任务的实体类，它实现了 Parcelable 接口，使得对象可以在不同的 Android 组件之间进行传递和序列化。
 */
public class SearchTask implements Parcelable {

    private int searchType; //用来表示搜索的类型，用于区分不同类型的搜索任务
    private int searchDuration;//表示搜索的持续时间

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 将对象的数据写入 Parcel 对象，以便序列化传递给其他组件
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.searchType);
        dest.writeInt(this.searchDuration);
    }

    public SearchTask() {
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public int getSearchDuration() {
        return searchDuration;
    }

    public void setSearchDuration(int searchDuration) {
        this.searchDuration = searchDuration;
    }

    protected SearchTask(Parcel in) {
        this.searchType = in.readInt();
        this.searchDuration = in.readInt();
    }

    /**
     * 实现了 Parcelable.Creator<SearchTask> 接口的静态字段，用于反序列化对象
     */
    public static final Creator<SearchTask> CREATOR = new Creator<SearchTask>() {
        public SearchTask createFromParcel(Parcel source) {
            return new SearchTask(source);
        }

        public SearchTask[] newArray(int size) {
            return new SearchTask[size];
        }
    };
}

/**
 * 使用 SearchTask 对象的示例，假设要在两个组件之间传递搜索任务对象
 * <p>
 * // 创建一个 SearchTask 对象并设置其属性
 * SearchTask task = new SearchTask();
 * task.setSearchType(1);
 * task.setSearchDuration(60); // 搜索持续 60 秒
 * <p>
 * // 将 SearchTask 对象传递给另一个组件，例如 Activity 或者 Service
 * Intent intent = new Intent(this, AnotherActivity.class);
 * intent.putExtra("search_task", task);
 * startActivity(intent);
 * <p>
 * 演示如何在另一个 Activity 中接收和解析 SearchTask 对象:
 * onCreate中
 * // 获取传递过来的 Intent
 * Intent intent = getIntent();
 * <p>
 * // 从 Intent 中获取 Parcelable 对象
 * SearchTask searchTask = intent.getParcelableExtra("search_task");
 * <p>
 * SearchTask 类通过实现 Parcelable 接口，使得它可以轻松地在 Android 应用的不同组件之间传递，并且保留了其属性的状态。
 * 这种方式在需要传递复杂对象数据时非常有用，尤其是在需要在 Activity、Service 和 Fragment 之间进行数据交换时。
 */
