package cz.muni.expense.parser;

import cz.muni.expense.enums.FileFormat;
import cz.muni.expense.enums.BankIdentifier;
import cz.muni.expense.exception.ParserException;
import cz.muni.expense.model.Payment;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Payments history parser for ČSOB bank
 *
 * @author Peter Petrinec
 */
@ParserType(bank = BankIdentifier.CSOB, format = FileFormat.XML)
public class CsobXmlParser implements Parser {

    @Override
    public List<Payment> parse(InputStream stream) throws ParserException {
        List<Payment> payments = new LinkedList<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stream);
            Locale czechLocale = Locale.forLanguageTag("cs");

            NodeList paymentsNodeList = doc.getElementsByTagName(CsobXmlStructure.PAYMENT);
            for (int i = 0; i < paymentsNodeList.getLength(); i++) {
                Node paymentNode = paymentsNodeList.item(i);
                if (paymentNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element paymentElement = (Element) paymentNode;
                    Payment payment = new Payment();

                    String date = paymentElement.getElementsByTagName(CsobXmlStructure.DATE).item(0).getTextContent();
                    String amount = paymentElement.getElementsByTagName(CsobXmlStructure.AMOUNT).item(0).getTextContent();
                    String info1 = paymentElement.getElementsByTagName(CsobXmlStructure.INFO_FOR_RECEIVER1).item(0).getTextContent();
                    String info2 = paymentElement.getElementsByTagName(CsobXmlStructure.INFO_FOR_RECEIVER2).item(0).getTextContent();

                    Date paymentDate = DateFormat.getDateInstance(DateFormat.SHORT, czechLocale).parse(date);
                    payment.setPaymentDate(paymentDate);
                    payment.setAmount(new BigDecimal(amount.replace(",", ".")));
                    payment.setInfoForReceiver1(info1.trim());
                    payment.setInfoForReceiver2(info2.trim());

                    payments.add(payment);
                }
            }
        } catch (SAXException | IOException | ParserConfigurationException | ParseException ex) {
            Logger.getLogger(CsobXmlParser.class.getName()).log(Level.SEVERE, null, ex);
            throw new ParserException("Failed to parse the file.", ex);
        }

        return payments;
    }

}
