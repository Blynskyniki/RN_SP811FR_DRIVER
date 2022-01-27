
## Installation

```sh
npm install rn-sp811fr-driver
```

## Usage

```typescript
export interface IRnSp811frDriverConnectOptions {
  host: string;
  port: number;
  password: string;
}
export declare enum IRnSp811frDriverDocumentType {
  NON_FISCAL_DOCUMENT = "1",
  SALE_DOCUMENT = "2",
  REFUND_DOCUMENT = "3",
  DEPOSITING_DOCUMENT = "4",
  WITCHDRAWAL_DOCUMENT = "5",
  OTHER = "6"
}
export declare enum IRnSp811frDriverVatType {
  VAT_20 = 0,
  VAT_10 = 1,
  VAT_0 = 2
}
export interface IRnSp811frDriverProduct {
  articul: string;
  nds: IRnSp811frDriverVatType;
  price: number;
  name: string;
  count: number;
  gtin: string;
}
export interface IRnSp811frDriverFrState {
  fatal: string;
  current: string;
  doc: string;
}
export declare enum IRnSp811frDriverDiscountType {
  PERCENT = "p",
  FOR_AMOUNT = "s"
}
export declare enum IRnSp811frDriverPaymentType {
  CASH = 0,
  CARD = 2
}
export interface IRnSp811frDriverDiscountData {
  type: IRnSp811frDriverDiscountType;
  percentOrSum: number;
  name: string;
}
export interface IRnSp811frDriverPaymentData {
  type: IRnSp811frDriverPaymentType;
  sum: number;
  text: string;
}
export declare class RnSp811frDriver {
  connect(options: IRnSp811frDriverConnectOptions): Promise<void>;
  disconnect(): Promise<void>;
  openDocument(type: IRnSp811frDriverDocumentType): Promise<void>;
  payment(type: IRnSp811frDriverPaymentData): Promise<void>;
  initFR(): Promise<void>;
  checkFr(): Promise<IRnSp811frDriverFrState>;
  cashInOutOperation(cashName: string, sumOrCount: number): Promise<IRnSp811frDriverFrState>;
  addProduct(data: IRnSp811frDriverProduct): Promise<void>;
  saleForDocOrProduct(data: IRnSp811frDriverDiscountData): Promise<void>;
  abortDocument(): Promise<void>;
  xReport(): Promise<void>;
  zReport(): Promise<void>;
  holdCheck(): Promise<void>;
  printCopyCheck(): Promise<void>;
  openCashDrawer(): Promise<void>;
  printText(data: string): Promise<void>;
  closeDocument(): Promise<void>;
  setNDS(): Promise<void>;
  subTotal(): Promise<void>;
  getFrParams(row: number, column: number): Promise<string>;
  setFrParams(row: number, column: number, value: string): Promise<void>;
}


```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
