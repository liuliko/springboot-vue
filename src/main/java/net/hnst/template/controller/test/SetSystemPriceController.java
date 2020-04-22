package net.hnst.template.controller.test;

import lombok.Cleanup;
import net.hnst.template.api.test.SetSystemPrice;
import net.hnst.template.controller.BaseController;
import net.hnst.template.service.test.SetSystemPriceService;
import net.hnst.template.util.ExcelFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping("/test")
@SuppressWarnings({"rawtypes", "unchecked"})
public class SetSystemPriceController extends BaseController {

	@Autowired
	private SetSystemPriceService setSystemPriceService;


	@RequestMapping(value = "/setSystemPrice/list.html", method = RequestMethod.POST)
	@ResponseBody
	public Object listTable(SetSystemPrice vo) {
		List<SetSystemPrice> list = setSystemPriceService.selectPage(vo);
		return getDataTableResultMap(list, vo);
	}


	@RequestMapping(value = "/setSystemPrice/insert.html", method = RequestMethod.POST)
	@ResponseBody
	public Object insert(SetSystemPrice vo) {
		int result;
		try {
			result = setSystemPriceService.insert(vo);
		} catch (Exception e) {
			e.printStackTrace();
			result = -1;
		}
		return getMessage(result);
	}

	@RequestMapping(value = "/setSystemPrice/update.html", method = RequestMethod.POST)
	@ResponseBody
	public Object update(SetSystemPrice vo) {
		int result;
		try {
			result = setSystemPriceService.updateByPrimaryKeySelective(vo);
		} catch (Exception e) {
			e.printStackTrace();
			result = -1;
		}
		return getMessage(result);
	}

	@RequestMapping(value = "/setSystemPrice/deletes.html", method = RequestMethod.POST)
	@ResponseBody
	public Object deletes(@RequestParam(value = "ids[]") List<String> ids) {
		int result = setSystemPriceService.deleteByBatch(ids);
		return getMessage(result);
	}


	@RequestMapping(value = "/setSystemPrice/export.html", method = RequestMethod.POST)
	public void export(SetSystemPrice vo, HttpServletResponse response) {
		List<SetSystemPrice> list = setSystemPriceService.selectList(vo);
		HSSFWorkbook workbook = new HSSFWorkbook();
		new ExcelFactory(workbook.createSheet("SetSystemPrice"), new String[]{
					"系统id",
					"商品id",
					"价格",
		}) {
			@Override
			protected Object[] setContents(Object o) {
				if (o instanceof SetSystemPrice) {
					SetSystemPrice setSystemPrice = (SetSystemPrice) o;
					Object[] contents = {
								setSystemPrice.getSystemId(),
								setSystemPrice.getCommondityId(),
								setSystemPrice.getPrice(),
					};
					return contents;
				}
				return new Object[0];
			}
		}.generateArrayTitle().generateContent(list);
		try {
			response.setHeader("content-disposition", "attachment;filename=" + new String("SetSystemPrice".getBytes("gbk"), "iso8859-1") + ".xls");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			@Cleanup OutputStream fOut = response.getOutputStream();
			workbook.write(fOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
