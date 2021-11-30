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



    private String getReceiptILines(List<ItemInfo> itemsContent, Map<String, Integer> itemQuantity) {
        List<String> receiptLines = new ArrayList<>();
        Arrays.stream(itemQuantity.keySet().toArray(new String[0])).sorted().forEach(itemId -> {
            ItemInfo itemDetail = getItemInfoById(itemId, itemsContent);
            receiptLines.add(generateReceiptItemLine(itemDetail, itemQuantity.get(itemDetail.getBarcode())));
        });
        return String.join("\n", receiptLines);
    }


}
