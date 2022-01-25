import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'rn-sp811fr-driver' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const RnSp811frDriver = NativeModules.RnSp811frDriver
  ? NativeModules.RnSp811frDriver
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function multiply(a: number, b: number): Promise<number> {
  return RnSp811frDriver.multiply(a, b);
}
