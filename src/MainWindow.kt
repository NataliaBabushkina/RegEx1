package ru.smak.regex

import ru.smak.regex.regex.RegexHelper
import java.awt.Color
import java.awt.Dimension
import java.io.*
import java.lang.Exception
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.text.BadLocationException
import javax.swing.text.DefaultHighlighter

class MainWindow : JFrame(){

    private val textBlock: JEditorPane
    private val textString: JTextField
    private val btnFindAddres: JButton
    private val btnFind: JButton
    private val btnSubstit: JButton
    private val btnOpen: JButton
    init{
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        minimumSize = Dimension(500, 500)
        textBlock = JEditorPane()
        btnFind = JButton()
        btnFind.text = "Найти"
        btnFind.addActionListener(){
           findSequence()
        }
        btnFindAddres=JButton()
        btnFindAddres.text="Найти e-mail"
        btnFindAddres.addActionListener {
    //        var rx="[^@\\s]+@[^@.\\s]+\\.[^@\\s]+"
            var rx="([0-9A-Za-z_-])[0-9A-Za-z_.+]*@(?:[0-9A-Za-z_-]+\\.)+[A-Za-z]{2,4}"
            find(rx)
        }
        textString = JTextField()
        btnSubstit=JButton()
        btnSubstit.text="Найти и заменить гиперссылки"
        btnSubstit.addActionListener{
            changeHyperlink()
        }
        btnOpen= JButton()
        btnOpen.text="Открыть файл"
        btnOpen.addActionListener {
            try{
                var filefilter=FileNameExtensionFilter(".txt", "txt")
                val d=JFileChooser()
                d.isAcceptAllFileFilterUsed=false
                d.fileFilter=filefilter
                d.currentDirectory= File(".")
                d.dialogTitle="Выбрать файл"
                d.approveButtonText="Выбрать"
                d.addChoosableFileFilter(filefilter)
                // Определяем фильтры типов файлов
                d.fileSelectionMode=JFileChooser.FILES_ONLY
                // Определение режима - только файлы
                val result=d.showOpenDialog(null)
                if (result==JFileChooser.APPROVE_OPTION){// если выбран файл
                    val fileInputStream= FileInputStream(d.selectedFile)
                    var bc=""
                    var i=fileInputStream.read()
                    while(i!=-1){
                        bc+=i.toChar()
                        i=fileInputStream.read()
                    }
                    textBlock.text=bc
                }
            }
            catch(e:Exception){}
        }
        val gl = GroupLayout(contentPane)
        layout = gl
        gl.setHorizontalGroup(
            gl.createSequentialGroup()
                .addGap(4)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(textBlock, 450, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addGroup(gl.createSequentialGroup()
                        .addComponent(textString, 250, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addGap(10)
                        .addComponent(btnFind, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(10)
                        .addComponent(btnOpen, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGroup(gl.createSequentialGroup()
                        .addComponent(btnSubstit, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(20)
                        .addComponent(btnFindAddres, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                )
                .addGap(4)
        )
        gl.setVerticalGroup(
            gl.createSequentialGroup()
                .addGap(4)
                .addComponent(textBlock, 400, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                .addGap(5)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(textString, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFind, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpen, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                )
                .addGap(5)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(btnSubstit, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFindAddres, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                )
                .addGap(5)

        )

        pack()
    }

    private fun find(rx: String) {
        val rh = RegexHelper()
        rh.regex = rx
        var txt = textBlock.text
        txt = txt.replace("\r", "")
        val result = rh.findIn(txt)
        val h = textBlock.highlighter
        val hp = DefaultHighlighter
            .DefaultHighlightPainter(Color.YELLOW)
        h.removeAllHighlights()
        for (res in result){
            try{
                h.addHighlight(res.first, res.second, hp)
            } catch (e: BadLocationException){}
        }
    }

    private fun findSequence(){
        var st=textString.text
        if (st.matches("[a-zA-Z]+".toRegex())){
            find("([^\\s]+)?"+st+"([^\\s]+)?")
        }
        else{
            if (!st.matches("[a-zA-Z\\s]+".toRegex())){
                find("(\\s)?"+st+"(\\s)?")
            }
        }
    }

    private fun changeHyperlink() {
        val rh = RegexHelper()
        rh.regex = "(?:https|http)(?:\\:\\/\\/)(?:[^ ]*)"
        var txt = textBlock.text
        txt = txt.replace("\r", "")
        val result = rh.findIn(txt)
        var changedText = txt
        for (res in result) {
            try {
                var ft = res.first
                var sd = res.second
                if (sd - ft > 40) {
                    var oldSubstr = txt.substring(ft, sd)
                    var newSubstr = txt.substring(ft, ft + 30) + "***" + txt.substring(sd - 10, sd)
                    changedText = changedText.replace(oldSubstr, newSubstr)
                }
            } catch (e: BadLocationException) {
            }
        }
        textBlock.text = changedText
        val result2 = rh.findIn(changedText)
        val h = textBlock.highlighter
        val hp = DefaultHighlighter
            .DefaultHighlightPainter(Color.YELLOW)
        h.removeAllHighlights()
        for (res in result2) {
            try {
                h.addHighlight(res.first, res.second, hp)
            } catch (e: BadLocationException) {
            }
        }
    }
}