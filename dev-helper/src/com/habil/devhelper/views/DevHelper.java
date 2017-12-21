package com.habil.devhelper.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.http.client.utils.URIBuilder;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import com.google.gson.Gson;
import com.habil.devhelper.dto.Matches;
import com.habil.devhelper.dto.Translate;
import com.habil.devhelper.utils.ColorUtil;
import com.habil.devhelper.utils.XmlFormatter;

public class DevHelper extends ViewPart {
    public static final String ID = "com.habil.devhelper.views.DevHelper";
    private Composite top = null;
    private TabFolder tabFolder = null;
    private Group groupUnicode, groupTranslate, groupFormatter, groupAbout = null;
    private Label unicodeTextLabel, labelTranslateInput, labelConverterType, labelAbout, labelVersion, labelFormatType = null;
    private Text unicodeText, unicodedText, textTranslateInput, textAreaTranslateResult, textAreaFormat = null;
    private Button unicodeButton, clearButton, buttonTranslate, buttonReplace, buttonClearTranslate, buttonFormat, buttonFormatClean = null;
    private Combo comboFrom, comboTo, comboFortmatType, comboConvertType = null;

    @Override
    public void createPartControl(Composite arg) {
        top = new Composite(arg, SWT.NO_SCROLL | SWT.NO_SCROLL);
        createTabFolder();
        top.setLayout(new GridLayout());
    }

    @Override
    public void setFocus() {
    }


    private void createTabFolder() {
        tabFolder = new TabFolder(top, SWT.H_SCROLL | SWT.V_SCROLL);
        tabFolder.setLayout(new GridLayout());
        tabFolder.setLayout(null);
        createGroupUnicode();
        createGroupTranslate();
        createGroupFormatter();
        createGroupAbout();

        TabItem tabItemUniCode = new TabItem(tabFolder, SWT.NONE);
        tabItemUniCode.setText("Unicode Çevirici");
        tabItemUniCode.setControl(groupUnicode);
        TabItem tabItemSozluk = new TabItem(tabFolder, SWT.NONE);
        tabItemSozluk.setText("Sözlük");
        tabItemSozluk.setControl(groupTranslate);
        TabItem tabItemFormatter = new TabItem(tabFolder, SWT.NONE);
        tabItemFormatter.setText("Formatter");
        tabItemFormatter.setControl(groupFormatter);
        TabItem tabItemAbout = new TabItem(tabFolder, SWT.NONE);
        tabItemAbout.setText("Hakkında");
        tabItemAbout.setControl(groupAbout);
    }

    /**
     * This method initializes groupUnicode	
     *
     */
    private void createGroupUnicode() {
        groupUnicode = new Group(tabFolder, SWT.SHADOW_OUT);
        groupUnicode.setLayout(null);
        groupUnicode.setBounds(new Rectangle(9, 29, 577, 260));
        unicodeTextLabel = new Label(groupUnicode, SWT.NONE);
        unicodeTextLabel.setText("Çevrilecek Metin :");
        unicodeTextLabel.setBounds(new Rectangle(3, 49, 116, 25));
        unicodeText = new Text(groupUnicode, SWT.BORDER);
        unicodeText.setBounds(new Rectangle(127, 48, 444, 26));
        unicodeText.setBackground(new Color(Display.getCurrent(), 221, 255, 221));
        unicodeButton = new Button(groupUnicode, SWT.NONE);
        unicodeButton.setText("Çevir");
        unicodeButton.setBounds(new Rectangle(423, 16, 71, 25));
        unicodeButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (!"".equals(unicodeText.getText())) {
                    unicodeText.setBackground(ColorUtil.getColor(SWT.COLOR_WIDGET_BACKGROUND));
                    String text = unicodeText.getText();
                    try {
                        if (comboConvertType.getSelectionIndex() == 0) {
                            unicodedText.setText(convertToNonTurkish(text));
                        } else {
                            unicodedText.setText(convertToUnicode(text));
                        }
                    } catch (Exception ex) {
                        System.err.println(ex.getMessage());
                    }
                } else {
                    unicodeText.setFocus();
                    unicodeText.setBackground(ColorUtil.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
                }
            }
        });
        unicodedText = new Text(groupUnicode, SWT.BORDER | SWT.V_SCROLL);
        unicodedText.setBounds(new Rectangle(4, 81, 567, 161));
        unicodedText.setBackground(new Color(Display.getCurrent(), 221, 255, 221));
        clearButton = new Button(groupUnicode, SWT.NONE);
        clearButton.setBounds(new Rectangle(500, 16, 71, 25));
        clearButton.setText("Temizle");
        labelConverterType = new Label(groupUnicode, SWT.NONE);
        labelConverterType.setBounds(new Rectangle(3, 17, 116, 25));
        labelConverterType.setText("Çevrim tipi seçiniz");
        createComboConvertType();
        comboConvertType.add("Türkçe karekter => İngilizce karakter", 0);
        comboConvertType.add("Türkçe karakter => Unicode karakter", 1);
        comboConvertType.select(0);
        clearButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                unicodeText.setText("");
                unicodedText.setText("");
            }
        });
    }

    /**
     * This method initializes groupTranslate	
     *
     */
    private void createGroupTranslate() {
        groupTranslate = new Group(tabFolder, SWT.SHADOW_OUT);
        groupTranslate.setLayout(null);
        groupTranslate.setBounds(new Rectangle(9, 294, 595, 250));
        labelTranslateInput = new Label(groupTranslate, SWT.NONE);
        labelTranslateInput.setText("Çevrilecek Metin :");
        labelTranslateInput.setBounds(new Rectangle(3, 15, 116, 25));
        textTranslateInput = new Text(groupTranslate, SWT.BORDER);
        textTranslateInput.setBounds(new Rectangle(124, 13, 465, 25));
        textTranslateInput.setBackground(new Color(Display.getCurrent(), 221, 255, 221));
        buttonTranslate = new Button(groupTranslate, SWT.NONE);
        buttonTranslate.setText("Çevir");
        buttonTranslate.setBounds(new Rectangle(467, 48, 58, 23));
        createComboFrom();
        createComboTo();
        buttonReplace = new Button(groupTranslate, SWT.NONE);
        buttonReplace.setBounds(new Rectangle(221, 48, 29, 23));
        buttonReplace.setText("<->");
        buttonReplace.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (comboFrom.getSelectionIndex() == 0) {
                    comboTo.select(0);
                    comboFrom.select(1);
                } else {
                    comboTo.select(1);
                    comboFrom.select(0);
                }
            }
        });
        comboFrom.add("Türkçe", 0);
        comboFrom.add("İngilizce", 1);
        comboFrom.select(0);
        comboTo.add("Türkçe", 0);
        comboTo.add("İngilizce", 1);
        comboTo.select(1);
        textAreaTranslateResult = new Text(groupTranslate, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
        textAreaTranslateResult.setBounds(new Rectangle(5, 78, 584, 154));
        textAreaTranslateResult.setBackground(new Color(Display.getCurrent(), 221, 255, 221));
        buttonClearTranslate = new Button(groupTranslate, SWT.NONE);
        buttonClearTranslate.setBounds(new Rectangle(531, 48, 58, 23));
        buttonClearTranslate.setText("Temizle");
        buttonClearTranslate.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                textTranslateInput.setText("");
                textAreaTranslateResult.setText("");
            }
        });
        buttonTranslate.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String translateText = textTranslateInput.getText();
                if (!"".equals(translateText)) {
                    textTranslateInput.setBackground(ColorUtil.getColor(SWT.COLOR_WIDGET_BACKGROUND));
                    String text = translateText.replace(" ", "+");
                    String langPair = getLangPair(comboFrom.getText()) + "|" + getLangPair(comboTo.getText());
                    Translate jsonFromUrl = getJSONFromUrl(createURI(text, langPair));
                    if (jsonFromUrl != null) {
                        String alter = "\n";
                        if (jsonFromUrl.getMatchList() != null && jsonFromUrl.getMatchList().size() > 0) {
                            for (Matches val : jsonFromUrl.getMatchList()) {
                                alter += val.getTranslation() + "\n";
                            }
                        }
                        textAreaTranslateResult.setText(jsonFromUrl.getResponseData().getTranslatedText() + "\n" + alter);
                    } else {
                    }
                } else {
                    textTranslateInput.setFocus();
                    textTranslateInput.setBackground(ColorUtil.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
                }
            }
        });
    }

    private static String getLangPair(String val) {
        return val.equals("Türkçe") ? "tr" : "en";
    }

    public static String createURI(String query, String langPair) {
        URIBuilder builder = new URIBuilder();
        URI url = null;
        try {
            builder.setScheme("http").setHost("www.mymemory.translated.net").setPath("/api/get").setParameter("q", query).setParameter("langpair", langPair);
            url = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return url.toString();
    }

    public Translate getJSONFromUrl(String url) {
        Translate translate = new Translate();
        try {
            InputStream is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            translate = new Gson().fromJson(readAll(rd).toString(), Translate.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return translate;
    }

    private static StringBuilder readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb;
    }

    private void createComboFrom() {
        comboFrom = new Combo(groupTranslate, SWT.NONE);
        comboFrom.setText("From");
        comboFrom.setBackground(new Color(Display.getCurrent(), 221, 255, 221));
        comboFrom.setBounds(new Rectangle(5, 48, 202, 23));
    }

    private void createComboTo() {
        comboTo = new Combo(groupTranslate, SWT.NONE);
        comboTo.setText("To");
        comboTo.setBackground(new Color(Display.getCurrent(), 221, 255, 221));
        comboTo.setBounds(new Rectangle(258, 48, 202, 23));
    }

    private void createGroupFormatter() {
        groupFormatter = new Group(tabFolder, SWT.SHADOW_OUT);
        groupFormatter.setLayout(null);
        groupFormatter.setBounds(new Rectangle(9, 549, 596, 253));
        labelFormatType = new Label(groupFormatter, SWT.NONE);
        labelFormatType.setBounds(new Rectangle(8, 17, 135, 25));
        labelFormatType.setText("Fotmat dilini seçiniz");
        createComboFormatType();
        buttonFormat = new Button(groupFormatter, SWT.NONE);
        buttonFormat.setBounds(new Rectangle(425, 17, 78, 25));
        buttonFormat.setText("Formatla");
        buttonFormat.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (!"".equals(textAreaFormat.getText())) {
                    textAreaFormat.setText(XmlFormatter.format(textAreaFormat.getText()));
                } else {
                    textAreaFormat.setFocus();
                    textAreaFormat.setBackground(ColorUtil.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
                }
            }
        });
        textAreaFormat = new Text(groupFormatter, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        textAreaFormat.setBounds(new Rectangle(8, 46, 582, 189));
        textAreaFormat.setBackground(new Color(Display.getCurrent(), 221, 255, 221));
        buttonFormatClean = new Button(groupFormatter, SWT.NONE);
        buttonFormatClean.setBounds(new Rectangle(516, 17, 74, 25));
        buttonFormatClean.setText("Temizle");
    }

    /**
     * This method initializes comboConvertType	
     *
     */
    private void createComboConvertType() {
        comboConvertType = new Combo(groupUnicode, SWT.NONE);
        comboConvertType.setText("Seçiniz");
        comboConvertType.setBackground(new Color(Display.getCurrent(), 221, 255, 221));
        comboConvertType.setBounds(new Rectangle(127, 17, 285, 23));
    }

    private String convertToUnicode(String text) {
        if (text.contains("ğ")) {
            text = text.replace("ğ", "\\u011f");
        }
        if (text.contains("Ğ")) {
            text = text.replace("Ğ", "\\u011e");
        }
        if (text.contains("ı")) {
            text = text.replace("ı", "\\u0131");
        }
        if (text.contains("İ")) {
            text = text.replace("İ", "\\u0130");
        }
        if (text.contains("ö")) {
            text = text.replace("ö", "\\u00f6");
        }
        if (text.contains("Ö")) {
            text = text.replace("Ö", "\\u00d6");
        }
        if (text.contains("ü")) {
            text = text.replace("ü", "\\u00fc");
        }
        if (text.contains("Ü")) {
            text = text.replace("Ü", "\\u00dc");
        }
        if (text.contains("ş")) {
            text = text.replace("ş", "\\u015f");
        }
        if (text.contains("Ş")) {
            text = text.replace("Ş", "\\u015e");
        }
        if (text.contains("ç")) {
            text = text.replace("ç", "\\u00e7");
        }
        if (text.contains("Ç")) {
            text = text.replace("Ç", "\\u00c7");
        }
        return text;
    }

    private String convertToNonTurkish(String text) {
        if (text.contains("ğ")) {
            text = text.replace("ğ", "g");
        }
        if (text.contains("Ğ")) {
            text = text.replace("Ğ", "G");
        }
        if (text.contains("ı")) {
            text = text.replace("ı", "i");
        }
        if (text.contains("İ")) {
            text = text.replace("İ", "I");
        }
        if (text.contains("ö")) {
            text = text.replace("ö", "o");
        }
        if (text.contains("Ö")) {
            text = text.replace("Ö", "O");
        }
        if (text.contains("ü")) {
            text = text.replace("ü", "u");
        }
        if (text.contains("Ü")) {
            text = text.replace("Ü", "U");
        }
        if (text.contains("ş")) {
            text = text.replace("ş", "s");
        }
        if (text.contains("Ş")) {
            text = text.replace("Ş", "S");
        }
        if (text.contains("ç")) {
            text = text.replace("ç", "c");
        }
        if (text.contains("Ç")) {
            text = text.replace("Ç", "C");
        }
        return text;
    }

    private void createComboFormatType() {
        comboFortmatType = new Combo(groupFormatter, SWT.NONE);
        comboFortmatType.setBackground(new Color(Display.getCurrent(), 221, 255, 221));
        comboFortmatType.setText("");
        comboFortmatType.setBounds(new Rectangle(153, 18, 257, 33));
        comboFortmatType.add("XML", 0);
        comboFortmatType.select(0);
    }


    private void createGroupAbout() {
        groupAbout = new Group(tabFolder, SWT.NONE);
        groupAbout.setLayout(null);
        labelAbout = new Label(groupAbout, SWT.NONE);
        labelAbout.setBounds(new Rectangle(5, 5, 635, 61));
        labelAbout.setText("Mevzu bahis olan bu plugin sırf \"Plugin işleri nasıl oluyormuş yeaa\" diye merak üzerine 22/10/2015 tarihinde yazılmıştır. \n \n Katkılarından dolayı bıyıklı arkadaşlarıma teşekkürlerimi sunuyorum.");
        labelVersion = new Label(groupAbout, SWT.NONE);
        labelVersion.setBounds(new Rectangle(13, 73, 232, 105));
        Bundle bundle = Platform.getBundle("dev-helper");
        Version version = bundle.getVersion();
        labelVersion.setText("DevHelper Versiyon : " + version + "\nKullanıcı : " + System.getenv().get("USERNAME") + "\nPC Adı : " + System.getenv().get("COMPUTERNAME"));
    }

} //  @jve:decl-index=0:visual-constraint="10,10,1132,554"
