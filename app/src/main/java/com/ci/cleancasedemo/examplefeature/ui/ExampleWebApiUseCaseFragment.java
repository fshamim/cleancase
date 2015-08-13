package com.ci.cleancasedemo.examplefeature.ui;

import android.app.Fragment;
import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import com.ci.cleancase.android.CleanFragment;
import com.ci.cleancase.framework.IClickable;
import com.ci.cleancasedemo.App;
import com.ci.cleancasedemo.R;
import com.ci.cleancasedemo.examplefeature.IWebApiInputPort;
import com.ci.cleancasedemo.examplefeature.resources.Todo;

import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExampleWebApiUseCaseFragment extends CleanFragment {

    @InjectView(R.id.tv_webcall_detail)
    TextView tvWebCallDetail;

    @InjectView(R.id.et_webcall_userid)
    EditText etWebCallUserId;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_web_call;
    }

    public void onEvent(IClickable.ClickEvent event) {
        int i = event.view.getId();
        if (i == R.id.btn_webcall_get) {
            mBus.post(new IWebApiInputPort.GetTodoByIdEvent(etWebCallUserId.getText().toString()));
        }
    }

    public void onEvent(IWebApiInputPort.TodoFoundEvent event) {
        Todo todo = event.todo;
        tvWebCallDetail.setText(todo.getTitle());
        tvWebCallDetail.setError(null);
    }

    public void onEvent(IWebApiInputPort.TodoNotFoundEvent event) {
        int statusCode = event.code;
        String reason = event.reason;
        final String errormsg = statusCode + ": " + reason;
        tvWebCallDetail.setText(errormsg);
        tvWebCallDetail.setError(errormsg);
    }

    @Override
    public void performInjection(Context context) {
        ((App) context.getApplicationContext()).getAppComponent().inject(this);
    }
}
