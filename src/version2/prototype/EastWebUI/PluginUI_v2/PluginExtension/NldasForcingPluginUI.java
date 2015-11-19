package version2.prototype.EastWebUI.PluginUI_v2.PluginExtension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.toedter.calendar.JDateChooser;

import version2.prototype.EastWebUI.DocumentBuilderInstance;
import version2.prototype.EastWebUI.PluginUI_v2.BasePlugin;
import version2.prototype.EastWebUI.PluginUI_v2.IPlugin;

public class NldasForcingPluginUI extends BasePlugin {
    private String freezingDate;
    private String coolingDegree;
    private String heatingDate;
    private String heatingDegree;

    public NldasForcingPluginUI(String PluginName, String QCLevel, ArrayList<String> Indicies) {
        super(PluginName, QCLevel, Indicies);
        // TODO Auto-generated constructor stub
    }

    public NldasForcingPluginUI() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void Save() {
        // TODO Auto-generated method stub
        SetFreezingDate(freezingDateChooser.getDate().toString());
        SetCoolingDegree(coolingTextField.getText());
        SetHeatingDate(heatingDateChooser.getDate().toString());
        SetHeatingDegree(heatingTextField.getText());
    }

    @Override
    public IPlugin GetParseObject(NodeList nodeList, int itemNumber) {
        NldasForcingPluginUI parsePlugin = null;

        try {
            File xmlFiles = new File("C:\\Users\\sufi\\git\\EastWeb.Version2\\projects\\New UI .xml");
            Document doc = DocumentBuilderInstance.Instance().GetDocumentBuilder().parse(xmlFiles);
            doc.getDocumentElement().normalize();
            nodeList = doc.getElementsByTagName("Plugin");

            parsePlugin = super.GetParseObject(nodeList.item(itemNumber), NldasForcingPluginUI.class);
            parsePlugin.SetFreezingDate(GetNodeListValuesIgnoreIfEmpty(((Element)nodeList.item(itemNumber))
                    .getElementsByTagName("FreezingDate")).get(0));
            parsePlugin.SetCoolingDegree(GetNodeListValuesIgnoreIfEmpty(((Element)nodeList.item(itemNumber))
                    .getElementsByTagName("CoolingDegree")).get(0));
            parsePlugin.SetHeatingDate(GetNodeListValuesIgnoreIfEmpty(((Element)nodeList.item(itemNumber))
                    .getElementsByTagName("HeatingDate")).get(0));
            parsePlugin.SetHeatingDegree(GetNodeListValuesIgnoreIfEmpty(((Element)nodeList.item(itemNumber))
                    .getElementsByTagName("HeatingDegree")).get(0));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return parsePlugin;
    }

    private void SetHeatingDegree(String string) {
        freezingDate = string;
    }

    private void SetHeatingDate(String string) {
        coolingDegree = string;
    }

    private void SetCoolingDegree(String string) {
        heatingDate = string;
    }

    private void SetFreezingDate(String string) {
        heatingDegree = string;
    }

    private String GetHeatingDegree() {
        return freezingDate;
    }

    private String GetHeatingDate() {
        return coolingDegree;
    }

    private String GetCoolingDegree() {
        return heatingDate;
    }

    private String GetFreezingDate() {
        return heatingDegree;
    }

    private JDateChooser freezingDateChooser;
    private JTextField coolingTextField;
    private JDateChooser heatingDateChooser;
    private JTextField heatingTextField;
    private JLabel lblNldasForcing;
    private JLabel lblFreezingStartDate;
    private JLabel lblHeatingStartDate;
    private JLabel lblCoolingDegreeThreshold;
    private JLabel lblHeatingDegreeThreshold;

    @Override
    public JPanel SetupUI(JPanel NldasForcingPanel, JFrame frame) {
        lblNldasForcing = new JLabel("Nldas Forcing");
        lblNldasForcing.setBounds(435, 41, 80, 14);
        NldasForcingPanel.add(lblNldasForcing);

        NldasForcingPanel.setLayout(null);
        NldasForcingPanel.setBounds(359, 420, 275, 390);

        lblFreezingStartDate = new JLabel("Freezing Date: ");
        lblFreezingStartDate.setBounds(338, 91, 71, 14);
        NldasForcingPanel.add(lblFreezingStartDate);
        freezingDateChooser = new JDateChooser();
        freezingDateChooser.setDateFormatString("MMM d");
        freezingDateChooser.setBounds(419, 89, 168, 20);
        NldasForcingPanel.add(freezingDateChooser);

        lblHeatingStartDate = new JLabel("Heating Date:");
        lblHeatingStartDate.setBounds(338, 158, 71, 14);
        NldasForcingPanel.add(lblHeatingStartDate);
        heatingDateChooser = new JDateChooser();
        heatingDateChooser.setDateFormatString("MMM d");
        heatingDateChooser.setBounds(419, 152, 168, 20);
        NldasForcingPanel.add(heatingDateChooser);

        lblCoolingDegreeThreshold = new JLabel("Cooling degree:");
        lblCoolingDegreeThreshold.setToolTipText("Cooling degree threshold");
        lblCoolingDegreeThreshold.setBounds(338, 125, 71, 14);
        NldasForcingPanel.add(lblCoolingDegreeThreshold);

        coolingTextField = new JTextField();
        coolingTextField.setBounds(419, 120, 100, 20);
        NldasForcingPanel.add(coolingTextField);
        coolingTextField.setColumns(10);

        lblHeatingDegreeThreshold = new JLabel("Heating degree");
        lblHeatingDegreeThreshold.setToolTipText("Heating degree threshold");
        lblHeatingDegreeThreshold.setBounds(338, 183, 71, 14);
        NldasForcingPanel.add(lblHeatingDegreeThreshold);

        heatingTextField = new JTextField();
        heatingTextField.setBounds(419, 180, 100, 20);
        NldasForcingPanel.add(heatingTextField);
        heatingTextField.setColumns(10);

        return NldasForcingPanel;
    }

    @Override
    public String GetUIDisplayPlugin()
    {
        String freezingstartDate = String.format("<br>FreezingDate: %s</span>",freezingDateChooser.getDate().toString());
        String coolingDegree = String.format("<br>CoolingDegree: %s</span>",coolingTextField.getText());
        String heatingstartDate = String.format("<br>HeatingDate: %s</span>",heatingDateChooser.getDate().toString());
        String heatingDegree = String.format("<br>HeatingDegree: %s</span>",heatingTextField.getText());

        String s = String.format("<html>%s%s%s%s%s</html>",super.GetUIDisplayPlugin(),
                freezingstartDate,
                coolingDegree,
                heatingstartDate,
                heatingDegree);
        return s;
    }

    @Override
    public Element GetXMLObject() throws ParserConfigurationException {

        Element p  = super.GetXMLObject();
        Element nldasForcing = DocumentBuilderInstance.Instance().GetDocument().createElement("NldasForcing");

        // Freezing start Date
        Element freezingstartDate = DocumentBuilderInstance.Instance().GetDocument().createElement("FreezingDate");
        freezingstartDate.appendChild(DocumentBuilderInstance.Instance().GetDocument().createTextNode(freezingDateChooser.getDate().toString()));
        nldasForcing.appendChild(freezingstartDate);

        // Cooling degree value
        Element coolingDegree = DocumentBuilderInstance.Instance().GetDocument().createElement("CoolingDegree");
        coolingDegree.appendChild(DocumentBuilderInstance.Instance().GetDocument().createTextNode(coolingTextField.getText()));
        nldasForcing.appendChild(coolingDegree);

        // Heating start Date
        Element heatingstartDate = DocumentBuilderInstance.Instance().GetDocument().createElement("HeatingDate");
        heatingstartDate.appendChild(DocumentBuilderInstance.Instance().GetDocument().createTextNode(heatingDateChooser.getDate().toString()));
        nldasForcing.appendChild(heatingstartDate);

        // heating degree value
        Element heatingDegree = DocumentBuilderInstance.Instance().GetDocument().createElement("HeatingDegree");
        heatingDegree.appendChild(DocumentBuilderInstance.Instance().GetDocument().createTextNode(heatingTextField.getText()));
        nldasForcing.appendChild(heatingDegree);

        p.appendChild(nldasForcing);

        return p;
    }

    @Override
    public void ClearUI(JPanel Panel) {
        Panel.remove(heatingTextField);
        Panel.remove(coolingTextField);
        Panel.remove(heatingDateChooser);
        Panel.remove(freezingDateChooser);
        Panel.remove(lblNldasForcing);
        Panel.remove(lblHeatingStartDate);
        Panel.remove(lblCoolingDegreeThreshold);
        Panel.remove(lblHeatingDegreeThreshold);
        Panel.remove(lblFreezingStartDate);
    }


}
