import {
  IRnSp811frDriverDiscountType,
  IRnSp811frDriverDocumentType,
  IRnSp811frDriverPaymentType,
  IRnSp811frDriverStatusQueries,
  IRnSp811frDriverVatType,
  RnSp811frDriver,
} from '@kari/rn-sp811fr-driver';
import React, { Dispatch, SetStateAction, useState } from 'react';
import {
  Alert,
  FlatList,
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

const driver = new RnSp811frDriver();
export function getEnumKeyByEnumValue(
  myEnum: any,
  enumValue: number | string
): string {
  let keys = Object.keys(myEnum).filter((x) => myEnum[x] == enumValue);
  return keys.length > 0 ? keys[0] : '';
}
const AlertError = (e: Error | unknown) => {
  console.error(e);
  Alert.alert('Ошибка', (e as Error).message, [
    { text: 'OK', onPress: () => console.log('OK') },
  ]);
};
const items: ICoolButtonProps[] = [
  {
    title: 'Продажа',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });

        await driver.initFR();

        await driver.openDocument(IRnSp811frDriverDocumentType.SALE_DOCUMENT);
        // сюда добавлять текст из заголовка
        await driver.printText('Заголовок, тут могла бы быть ваша реклама');

        await driver.addProduct({
          articul: '00001851',
          count: 150,
          gtin: '00185158484',
          nds: IRnSp811frDriverVatType.VAT_20,
          name: 'Супер товар',
          price: 0.35,
        });
        await driver.saleForDocOrProduct({
          name: 'Скидка на сумму',
          percentOrSum: 59,
          type: IRnSp811frDriverDiscountType.FOR_AMOUNT,
        });
        await driver.addProduct({
          articul: '00001852',
          count: 50.5,
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
        // сюда добавлять текст из подвала

        await driver.printText('Подвал, тут могла бы быть ваша реклама');

        await driver.closeDocument();
        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Возврат',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });

        await driver.initFR();

        await driver.openDocument(IRnSp811frDriverDocumentType.REFUND_DOCUMENT);

        await driver.addProduct({
          articul: '00001851',
          count: 150,
          gtin: '00185158484',
          nds: IRnSp811frDriverVatType.VAT_20,
          name: 'Супер товар',
          price: 150,
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
        console.error('Отмена', e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Недоделанный чек',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
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

        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Отмена документа (чек завис)',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });
        await driver.abortDocument();
        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Z Отчет',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });
        await driver.initFR();

        await driver.zReport();
        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'X Отчет',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });
        await driver.initFR();

        await driver.xReport();
        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Открыть денежный ящик',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });

        await driver.openCashDrawer();

        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Печать текста ',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });

        await driver.printText('Печать текста ');

        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Инкасация ',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });

        await driver.openDocument(
          IRnSp811frDriverDocumentType.WITCHDRAWAL_DOCUMENT
        );
        await driver.cashInOutOperation('Купюры по 5К', 100);
        await driver.closeDocument();

        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Отложить чек ',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });

        await driver.holdCheck();

        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Копия чека ',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });

        await driver.printCopyCheck();

        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Проверка состояния фискального регистратора ',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });

        const res = await driver.checkFr();
        Alert.alert(
          'Проверка состояния фискального регистратора',
          JSON.stringify(res),
          [{ text: 'OK', onPress: () => console.log('OK Pressed') }]
        );
        console.log('Проверка состояния фискального регистратора ', res);

        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Пополнение кассы ',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });

        await driver.openDocument(
          IRnSp811frDriverDocumentType.DEPOSITING_DOCUMENT
        );
        await driver.cashInOutOperation('Купюры по 5К', 100);
        await driver.closeDocument();

        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Считать настройки ндс',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });

        await driver.initFR();
        const res = await driver.getFrParams(12, 0);
        Alert.alert('Настройки ндс', JSON.stringify(res), [
          { text: 'OK', onPress: () => console.log('OK Pressed') },
        ]);
        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },

  {
    title: 'Настроить печать НДС',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });

        await driver.initFR();
        await driver.zReport();
        await driver.setFrParams(12, 0, '30');

        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Настроить Типы оплат',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });

        await driver.initFR();
        await driver.zReport();
        await driver.setFrParamCyr(22, 2, 'БАНКОВСКАЯ КАРТА');
        await driver.setFrParamCyr(22, 1, 'НАЛИЧНЫЕ');
        await driver.setFrParamCyr(22, 1, 'ПОДАРОЧНАЯ КАРТА');

        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Настроить Клише (Картинка)',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });

        // Включить заголовок (Достаточно 1 раз сделать)
        await driver.setFrParams(5, 0, '1');
        // await driver.setHeaderImage(
        //   'https://cdn.api.kari.com/f/SP801_Logo_ServPlus_koordinats.bmp'
        // );
        // Проверка ошибки размера картинки
        await driver.setHeaderImage('https://cdn.api.kari.com/f/logotype.bmp');

        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Установить верхнее и нижнее Клише',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });

        await driver.setHeaderTxt([
          'Строка 1',
          'Строка 2',
          'Строка 3',
          'Строка 4',
        ]);
        await driver.initFR();
        await driver.setFooterTxt(['Строка 1', 'Строка 2']);
        await driver.initFR();
        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },
  {
    title: 'Получение информации из ККМ',
    onPress: async (host, port) => {
      try {
        await driver.connect({
          port,
          host,
          password: 'PONE',
        });
        let dataMap = '';

        const keys = Object.keys(IRnSp811frDriverStatusQueries).filter((v) =>
          Number.isNaN(+v)
        );
        for (const settingKey of keys) {
          console.info(settingKey, IRnSp811frDriverStatusQueries[settingKey]);
          try {
            dataMap += `${settingKey} ${await driver.getData(
              +IRnSp811frDriverStatusQueries[settingKey]
            )} \n`;
          } catch (e) {
            console.error(
              settingKey,
              IRnSp811frDriverStatusQueries[settingKey],
              e
            );
          }
        }
        console.log(dataMap);

        Alert.alert('Параметры', dataMap, [
          { text: 'OK', onPress: () => console.log('OK Pressed') },
        ]);
        await driver.disconnect();
      } catch (e) {
        AlertError(e);
        await driver.disconnect();
      }
    },
  },
];

interface ICoolButtonProps {
  title: string;
  onPress: (host: string, port: number) => void;
  ip?: string;
  port?: number;
}

const CoolButton = ({ title, onPress, ip, port }: ICoolButtonProps) => {
  const isDarkMode = useColorScheme() === 'dark';

  return (
    <View style={styles.margin}>
      <View
        style={[
          styles.separator,
          {
            marginVertical: 5,
            backgroundColor: isDarkMode ? Colors.dark : Colors.light,
          },
        ]}
      />
      <TouchableOpacity
        accessibilityRole="button"
        onPress={() => onPress(ip!, +port!)}
        style={styles.linkContainer}
      >
        <Text
          style={[
            styles.description,
            {
              color: Colors.white,
            },
          ]}
        >
          {title}
        </Text>
      </TouchableOpacity>
    </View>
  );
};
const RenderItem = ({
  item,
  ip,
  port,
}: {
  item: ICoolButtonProps;
  ip: string;
  port: string;
}) => {
  return (
    // @ts-ignore
    <CoolButton title={item.title} onPress={item.onPress} ip={ip} port={port} />
  );
};

interface IButton {
  ip: string;
  port: string;
}

const Buttons = ({ ip, port }: IButton) => {
  return (
    <FlatList
      data={items}
      renderItem={({ item }) => {
        return <RenderItem item={item} ip={ip} port={port} />;
      }}
      keyExtractor={(_, key) => key.toString()}
      style={{ flex: 1 }}
      showsVerticalScrollIndicator={false}
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
    <View>
      <Text style={{ color: Colors.primary }}>{title}</Text>
      <TextInput
        placeholder={title}
        onChangeText={onChangeText}
        value={value}
        style={[
          styles.input,
          {
            borderBottomWidth: 2,
            borderColor: Colors.primary,
          },
        ]}
      />
    </View>
  );
};

interface IPort {
  onChangeTextIp: Dispatch<SetStateAction<string>>;
  onChangeTextPort: Dispatch<SetStateAction<string>>;
  ip: string;
  port: string;
}

const IPPort = ({ onChangeTextIp, onChangeTextPort, ip, port }: IPort) => {
  const isDarkMode = useColorScheme() === 'dark';

  return (
    <View>
      <InputWithLabel value={ip} onChangeText={onChangeTextIp} title={'IP'} />
      <InputWithLabel
        value={port}
        onChangeText={onChangeTextPort}
        title={'PORT'}
      />
      <View
        style={[
          {
            backgroundColor: isDarkMode ? Colors.dark : Colors.light,
          },
        ]}
      />
      <Text style={{ paddingVertical: 5 }}> Настройки: </Text>
    </View>
  );
};

const App = () => {
  const isDarkMode = useColorScheme() === 'dark';
  const [ip, setIP] = useState('192.168.2.160');
  const [port, setPort] = useState('9876');

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
    padding: 10,
    flex: 1,
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <IPPort
        onChangeTextPort={setPort}
        onChangeTextIp={setIP}
        port={port}
        ip={ip}
      />
      <Buttons ip={ip} port={port} />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {},
  linkContainer: {
    alignItems: 'center',
    paddingVertical: 5,
    borderRadius: 10,
    backgroundColor: 'rgba(3,125,152,0.65)',
  },
  description: {
    paddingVertical: 10,
    fontWeight: '400',
    fontSize: 18,
    textAlign: 'center',
  },
  margin: {},
  input: {
    padding: 2,
    marginBottom: 5,
  },
  separator: {},
});

export default App;
