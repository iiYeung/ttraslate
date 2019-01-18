import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.util.text.StringUtil;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

public class HttpTestAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        assert editor != null;

        SelectionModel selectionModel = editor.getSelectionModel();

        String selectText = selectionModel.getSelectedText();

        if(StringUtil.isEmpty(selectText)){
            return;
        }

        System.out.println(selectText);

        OkHttpClient client = new OkHttpClient();

        String translateApi = "https://openapi.youdao.com/api";

        String url = "https://searchcode.com/api/jsonp_codesearch_I/?callback=code&q="+selectText;

        Request request = new Request.Builder()
            .url(url)
            .build();

        try {
            Response response = client.newCall(request).execute();

            assert response.body() != null;
            System.out.println(response.body().string());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
