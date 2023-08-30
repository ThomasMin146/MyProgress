package com.thomas.myprogress;

public interface RVInterface {
    void onItemClick(int position);

    void onAddItemClick(int position);

    void onLongItemClick(int position, boolean isChecked);
}
