package main;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bean.ManufacturerKey;
import com.dao.IManufacturerKeyDao;

public class IManufacturerKeyDaoTest {

	public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "spring-applicationContext.xml");
        String[] str=context.getBeanDefinitionNames();

        IManufacturerKeyDao manufacturerKeyDao = (IManufacturerKeyDao)context.getBean("IManufacturerKeyDao");
        List<ManufacturerKey> otaCommKeyList = manufacturerKeyDao.getKeyByType("1");
        System.out.println("ManufacturerCode|KeyType|KeyIndex|KeyValue密文|KeyValue明文");
        for(ManufacturerKey key:otaCommKeyList){
        	System.out.println(key.getManufacturerCode() +  "|" + key.getKeyType() + "|" + key.getKeyIndex() + "|" + key.getKeyValue());
        }
	}
	
}
