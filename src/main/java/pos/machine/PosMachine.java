package pos.machine;

import java.util.*;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        List<ItemInfo> itemsContent = ItemDataLoader.loadAllItemInfos();
        return generateReceipt(barcodes, itemsContent);
    }

    private String generateReceipt(List<String> barcodesList, List<ItemInfo> itemsContent) {
        String receipt = getReceiptHeader();
        Map<String, Integer> itemQuantity = getQuantity(barcodesList.toArray(new String[0]));
        receipt += getReceiptILines(itemsContent, itemQuantity) + "\n";
        Integer total = calculateTotal(itemsContent, itemQuantity);
        receipt += getReceiptFooter(total);
        return receipt;
    }



}
