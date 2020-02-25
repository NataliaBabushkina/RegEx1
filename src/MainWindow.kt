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
    private val btnFind: JButton
    private val btnSave: JButton
    private val btnOpen: JButton
    init{
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        minimumSize = Dimension(500, 500)
        textBlock = JEditorPane()
        btnFind = JButton()
        btnFind.text = "Найти адрес"
        btnFind.addActionListener { find() }
        btnSave= JButton()
        btnSave.text="Сохранить"
        btnSave.addActionListener {
            try{
                var filefilter=FileNameExtensionFilter(".txt", "txt")
                var s: String? = null
                val d=JFileChooser()
                d.isAcceptAllFileFilterUsed=false
                d.fileFilter=filefilter
                d.currentDirectory= File(".")
                d.dialogTitle="Сохранить файл"
                d.approveButtonText="Сохранить"
                val res=d.showSaveDialog(parent)
                if (res==JFileChooser.APPROVE_OPTION){
                    s=d.selectedFile.absolutePath ?: ""
                    if (!d.fileFilter.accept(d.selectedFile)){
                        s+="."+(filefilter?.extensions?.get(0)?:"")
                    }
                }
                val fileOutputStream = FileOutputStream(s)
                fileOutputStream.write(textBlock.text.toByteArray())
                fileOutputStream.close()
            }
            catch(e: Exception){}
        }
        btnOpen= JButton()
        btnOpen.text="Открыть"
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
                        .addComponent(btnOpen, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(10)
                        .addComponent(btnFind, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(10)
                        .addComponent(btnSave, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                )
                .addGap(4)
        )
        gl.setVerticalGroup(
            gl.createSequentialGroup()
                .addGap(4)
                .addComponent(textBlock, 400, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                .addGap(4)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(btnOpen, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFind, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                )
                .addGap(4)
        )

        pack()
    }

    private fun find() {
        val rh = RegexHelper()
        rh.regex = "[^@\\s]+@[^@.\\s]+\\.[^@\\s\\.]+"
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
}

