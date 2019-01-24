import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.popup.Balloon.Position;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.JBColor;
import groovy.util.logging.Slf4j;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import util.YouDaoUtil;

@Slf4j
public class Ttr extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        //  获取editor对象
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        assert editor != null;

        //  获取选择文本

        SelectionModel selectionModel = editor.getSelectionModel();

        String selectText = selectionModel.getSelectedText();

        if (StringUtil.isEmpty(selectText)) {
            return;
        }

        System.out.println(selectText);

        try {
            String result = YouDaoUtil.query(selectText);

            System.out.println(selectText);

            assert result != null;

            JsonObject jsonObject = new Gson().fromJson(result, JsonObject.class);

            if (!Objects.isNull(jsonObject)) {
                JsonArray array = jsonObject.getAsJsonArray("translation");

                Iterator<JsonElement> iterator = array.iterator();

                StringBuilder sb = new StringBuilder();

                while (iterator.hasNext()) {
                    sb.append("<p>")
                        .append(iterator.next().getAsString())
                        .append("</p>");
                }

                //  获取默认弹窗
                JBPopupFactory instance = JBPopupFactory.getInstance();

                BalloonBuilder builder = instance.createHtmlTextBalloonBuilder(sb.toString(), null,
                    new JBColor(new Color(242, 242, 242), new Color(190, 190, 190)), null);

                builder
//                    .setTitle("codelf")
                    .setClickHandler(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            e.getActionCommand();
                        }
                    }, true)
                    .createBalloon()
                    .show(instance.guessBestPopupLocation(editor), Position.below);

            }


        } catch (IOException e1) {
            e1.printStackTrace();
        }

//        OkHttpClient client = new OkHttpClient();
//
//        String translateApi = "https://openapi.youdao.com/api";
//
//        String url = "https://searchcode.com/api/jsonp_codesearch_I/?callback=code&q="+selectText;
//
//        Request request = new Request.Builder()
//            .url(url)
//            .build();
//
//        try {
//            Response response = client.newCall(request).execute();
//
//            assert response.body() != null;
//            System.out.println(response.body().string());
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
    }
}
