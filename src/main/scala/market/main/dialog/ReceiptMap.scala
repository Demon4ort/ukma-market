package market.main.dialog

import market.main.receipt.Receipt
import market.main.sale.Sale

case class ReceiptMap(receipt: Receipt, sales: Seq[Sale])
