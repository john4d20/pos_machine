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

    private String generateReceiptItemLine(ItemInfo itemDetail, Integer quantity) {
        return String.format("Name: %s, Quantity: %d, Unit price: %d (yuan), Subtotal: %d (yuan)", itemDetail.getName(), quantity, itemDetail.getPrice(), calculateItemSubtotal(itemDetail, quantity));
    }

    private Map<String, Integer> getQuantity(String[] items) {
        Map<String, Integer> quantityMap = new HashMap<>();
        Arrays.stream(items).forEach(item -> {
            if (quantityMap.containsKey(item)) {
                quantityMap.put(item, quantityMap.get(item) + 1);
            }
            else {
                quantityMap.put(item, 1);
            }
        });
        return quantityMap;
    }

    private Integer calculateItemSubtotal(ItemInfo itemDetail, Integer quantity) {
        return itemDetail.getPrice() * quantity;
    }

    private Integer calculateTotal(List<ItemInfo> itemsContent, Map<String, Integer> itemQuantity) {
        Integer total = 0;
        for (String itemId: itemQuantity.keySet()) {
            total += calculateItemSubtotal(getItemInfoById(itemId, itemsContent), itemQuantity.get(itemId));
        }
        return total;
    }

    private ItemInfo getItemInfoById(String id, List<ItemInfo> allItemsContent) {
        for (ItemInfo item: allItemsContent) {
            if (item.getBarcode().equals(id)) return item;
        }
        return null;
    }

    private String getReceiptFooter(Integer total) {
        return String.format(
                "----------------------\n" +
                        "Total: %d (yuan)\n" +
                        "**********************", total);
    }

    private String getReceiptHeader() {
        return "***<store earning no money>Receipt***\n";
    }
}
