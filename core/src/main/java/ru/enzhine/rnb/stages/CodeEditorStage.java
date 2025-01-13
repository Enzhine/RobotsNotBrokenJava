//package ru.enzhine.rnb.stages;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
//import ru.enzhine.rnb.editor.CodeEditor;
//import ru.enzhine.rnb.editor.CodeEditorInputProcessor;
//
//public class CodeEditorStage extends Stage {
//
//    private final Table table;
//
//    public CodeEditorStage() {
//        super();
//        this.table = new Table();
//        addActor(this.table);
//
////        Skin skin = new Skin(Gdx.files.internal("ui/skin.json"));
//
//        this.table.setFillParent(true);
//        this.table.setDebug(true);
//
//        CodeEditor ce = new CodeEditor();
//        table.add(ce);
//
//        Gdx.input.setInputProcessor(new CodeEditorInputProcessor(ce));
//    }
//
//    @Override
//    public void draw() {
//        super.draw();
//    }
//
//    @Override
//    public void dispose() {
//        super.dispose();
//    }
//}
