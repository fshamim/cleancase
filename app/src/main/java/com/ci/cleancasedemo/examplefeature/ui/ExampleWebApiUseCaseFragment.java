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
import com.ci.cleancasedemo.examplefeature.interactor.ExampleWebApiUseCaseInteractor;
import com.ci.cleancasedemo.examplefeature.resources.Todo;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExampleWebApiUseCaseFragment extends CleanFragment {

    @InjectView(R.id.tv_webcall_detail)
    TextView tvWebCallDetail;

    @InjectView(R.id.et_webcall_userid)
    EditText etWebCallUserId;

    @Inject
    ExampleWebApiUseCaseInteractor exampleWebApiUseCaseInteractor;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_web_call;
    }

    @Subscribe
    public void onEvent(IClickable.ClickEvent event) {
        int i = event.view.getId();
        if (i == R.id.btn_webcall_get) {

//            mBus.post(new UnknownErrorEvent(120,"is working"));
            mBus.post(new IWebApiInputPort.GetTodoByIdEvent(etWebCallUserId.getText().toString()));
        }
    }
    @Subscribe
    public void onEvent(IWebApiInputPort.TodoFoundEvent event) {
        Todo todo = event.todo;
        tvWebCallDetail.setText(todo.getTitle());
        tvWebCallDetail.setError(null);
    }
    @Subscribe
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
