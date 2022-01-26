import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'rn-sp811fr-driver' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const Driver = NativeModules.RnSp811frDriver
  ? NativeModules.RnSp811frDriver
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export interface IRnSp811frDriverConnectOptions {
  host: string;
  port: number;
  password: string;
}

export enum IRnSp811frDriverDocumentType {
  NON_FISCAL_DOCUMENT = '1',
  SALE_DOCUMENT = '2',
  REFUND_DOCUMENT = '3',
  DEPOSITING_DOCUMENT = '4',
  WITCHDRAWAL_DOCUMENT = '5',
  OTHER = '6',
}
export enum IRnSp811frDriverVatType {
  VAT_20 = 0,
  VAT_10 = 1,
  VAT_0 = 2,
}
export interface IRnSp811frDriverProduct {
  articul: string;
  nds: IRnSp811frDriverVatType;
  price: number;
  name: string;
  count: number;
  gtin: string;
  // не реализованно
  // gtinType?: string;
}

export class RnSp811frDriver {
  public connect(options: IRnSp811frDriverConnectOptions): Promise<void> {
    return Driver.connect(options);
  }

  public disconnect(): Promise<void> {
    return Driver.disconnect();
  }

  public openDocument(type: IRnSp811frDriverDocumentType): Promise<void> {
    return Driver.openDocument(type);
  }

  public initFR(): Promise<void> {
    return Driver.initFR();
  }

  public addProduct(data: IRnSp811frDriverProduct): Promise<void> {
    return Driver.addProduct(data);
  }

  public abortDocument(): Promise<void> {
    return Driver.abortDocument();
  }

  public xReport(): Promise<void> {
    return Driver.xReport();
  }

  public zReport(): Promise<void> {
    return Driver.zReport();
  }

  public holdCheck(): Promise<void> {
    return Driver.holdCheck();
  }

  public printCopyCheck(): Promise<void> {
    return Driver.printCopyCheck();
  }

  public openCashDrawer(): Promise<void> {
    return Driver.openCashDrawer();
  }

  public printText(data: string): Promise<void> {
    return Driver.printText(data);
  }

  public closeDocument(): Promise<void> {
    return Driver.closeDocument();
  }

  public setNDS(): Promise<void> {
    return Driver.setNDS();
  }

  public subTotal(): Promise<void> {
    return Driver.subTotal();
  }
}
