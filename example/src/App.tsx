/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React, { Dispatch, SetStateAction, useState } from 'react';
import {
  FlatList,
  ListRenderItemInfo,
  SafeAreaView,
  StatusBar,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  useColorScheme,
  View,
} from 'react-native';

import { Colors } from 'react-native/Libraries/NewAppScreen';
import {
  IRnSp811frDriverDiscountType,
  IRnSp811frDriverDocumentType,
  IRnSp811frDriverPaymentType,
  IRnSp811frDriverVatType,
  RnSp811frDriver,
} from 'rn-sp811fr-driver';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
const driver = new RnSp811frDriver();

const items: ICoolButtonProps[] = [
  {
    title: 'Продажа',
    onPress: async () => {
      try {
        await driver.connect({
          port: 9876,
          host: '192.168.2.150',
          password: 'PONE',
        });

        await driver.initFR();

        await driver.openDocument(IRnSp811frDriverDocumentType.SALE_DOCUMENT);

        await driver.addProduct({
          articul: '00001851',
          count: 150,
          gtin: '00185158484',
          nds: IRnSp811frDriverVatType.VAT_20,
          name: 'Супер товар',
          price: 150,
        });
        await driver.saleForDocOrProduct({
          name: 'Скидка на сумму',
          percentOrSum: 59,
          type: IRnSp811frDriverDiscountType.FOR_AMOUNT,
        });
        await driver.addProduct({
          articul: '00001852',
          count: 50,
          gtin: '00001851284',
          nds: IRnSp811frDriverVatType.VAT_10,
          name: 'Супер товар2',
          price: 150,
        });
        await driver.saleForDocOrProduct({
          name: 'Скидка на процент',
          percentOrSum: 15,
          type: IRnSp811frDriverDiscountType.PERCENT,
        });
        await driver.subTotal();

        await driver.setNDS();

        await driver.subTotal();
        await driver.payment({
          sum: 20000,
          text: 'Денежка ',
          type: IRnSp811frDriverPaymentType.CASH,
        });
        await driver.closeDocument();
        await driver.disconnect();
      } catch (e) {
        console.error('Продажа', e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Отмена документа',
    onPress: async () => {
      try {
        await driver.connect({
          port: 9876,
          host: '192.168.2.150',
          password: 'PONE',
        });
        await driver.abortDocument();
        await driver.disconnect();
      } catch (e) {
        console.error(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Z Отчет',
    onPress: async () => {
      try {
        await driver.connect({
          port: 9876,
          host: '192.168.2.150',
          password: 'PONE',
        });
        await driver.initFR();

        await driver.zReport();
        await driver.disconnect();
      } catch (e) {
        console.error(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'X Отчет',
    onPress: async () => {
      try {
        await driver.connect({
          port: 9876,
          host: '192.168.2.150',
          password: 'PONE',
        });
        await driver.initFR();

        await driver.xReport();
        await driver.disconnect();
      } catch (e) {
        console.error(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Открыть денежный ящик',
    onPress: async () => {
      try {
        await driver.connect({
          port: 9876,
          host: '192.168.2.150',
          password: 'PONE',
        });

        await driver.openCashDrawer();

        await driver.disconnect();
      } catch (e) {
        console.error(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Печать текста ',
    onPress: async () => {
      try {
        await driver.connect({
          port: 9876,
          host: '192.168.2.150',
          password: 'PONE',
        });

        await driver.printText('Печать текста ');

        await driver.disconnect();
      } catch (e) {
        console.error(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Инкасация ',
    onPress: async () => {
      try {
        await driver.connect({
          port: 9876,
          host: '192.168.2.150',
          password: 'PONE',
        });

        await driver.openDocument(
          IRnSp811frDriverDocumentType.DEPOSITING_DOCUMENT
        );
        await driver.cashInOutOperation('Купюры по 5К', 100);
        await driver.closeDocument();

        await driver.disconnect();
      } catch (e) {
        console.error(e);
        await driver.disconnect();
      }
    },
  },
];

interface ICoolButtonProps {
  title: string;
  onPress: () => void;
}

const CoolButton = ({ title, onPress }: ICoolButtonProps) => {
  const isDarkMode = useColorScheme() === 'dark';

  return (
    <View style={styles.margin}>
      <View
        style={[
          styles.separator,
          {
            backgroundColor: isDarkMode ? Colors.dark : Colors.light,
          },
        ]}
      />
      <TouchableOpacity
        accessibilityRole="button"
        onPress={onPress}
        style={styles.linkContainer}
      >
        <Text
          style={[
            styles.description,
            {
              color: Colors.dark,
            },
          ]}
        >
          {title}
        </Text>
      </TouchableOpacity>
    </View>
  );
};
const renderItem = ({
  item: { title, onPress },
}: ListRenderItemInfo<ICoolButtonProps>) => {
  return <CoolButton title={title} onPress={onPress} />;
};

const Biba = () => {
  return (
    <FlatList
      data={items}
      renderItem={renderItem}
      keyExtractor={(_, key) => key.toString()}
    />
  );
};

interface IInputWithLabel {
  title: string;
  value: string;
  onChangeText: Dispatch<SetStateAction<string>>;
}

const InputWithLabel = ({ title, value, onChangeText }: IInputWithLabel) => {
  return (
    <View style={styles.input}>
      <TextInput
        placeholder={title}
        onChangeText={onChangeText}
        value={value}
      />
    </View>
  );
};

const IPPort = () => {
  const isDarkMode = useColorScheme() === 'dark';

  const [ip, setIP] = useState('192.168.2.150');
  const [port, setPort] = useState('9876');

  return (
    <View>
      <InputWithLabel value={ip} onChangeText={setIP} title={'IP'} />
      <InputWithLabel value={port} onChangeText={setPort} title={'PORT'} />
      <View
        style={[
          styles.separator,
          {
            backgroundColor: isDarkMode ? Colors.dark : Colors.light,
          },
        ]}
      />
      <Text> Настройки: </Text>
    </View>
  );
};

const App = () => {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
    padding: 10,
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <IPPort />
      <Biba />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {},
  linkContainer: {
    alignItems: 'center',
    paddingVertical: 8,
  },
  description: {
    paddingVertical: 16,
    fontWeight: '400',
    fontSize: 18,
  },
  margin: {},
  input: {
    padding: 2,
  },
  separator: {
    height: StyleSheet.hairlineWidth,
  },
});

export default App;
