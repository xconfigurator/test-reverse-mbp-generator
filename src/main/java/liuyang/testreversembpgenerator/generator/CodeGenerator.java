package liuyang.testreversembpgenerator.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 文档参考：https://baomidou.com/guide/generator.html
 * 视频参考：https://www.imooc.com/video/22857
 * @author liuyang
 * @scine 2021/4/6
 */
@Slf4j
public class CodeGenerator {

    private static final String PACKAGE_CONFIG_PARENT = "liuyang.testpkg";
    private static final String PACKAGE_CONFIG_MODULE_NAME = "testmodule";

    // scanner("表名，多个英文逗号分割").split(",")
    // private static final String STRATEGY_TABLE_NAME = "tb_pdt_area_info, tb_pdt_task_info";
    private static final String STRATEGY_TABLE_NAME = "tb_pdt_arae_info,tb_pdt_task_info";

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        //gc.setAuthor("jobob");
        gc.setAuthor("liuyang");
        // gc.setOpen(false);
        gc.setOpen(true);
        // swagger
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        gc.setSwagger2(true);
        // liuyang 日期类型
        // java.util java.sql java.time 详见代码注释 默认java.time DateType.TIME_PACK
        gc.setDateType(DateType.TIME_PACK);
        // liuyang 生成代码规则
        gc.setServiceName("%sService"); // 默认会在接口前加一个I
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        // dsc.setUrl("jdbc:mysql://localhost:3306/ant?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dsc.setUrl("jdbc:mysql://localhost:3306/pdt-nms?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=GMT%2B8");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        // pc.setModuleName(scanner("模块名"));
        pc.setModuleName(PACKAGE_CONFIG_MODULE_NAME);
        log.info("模块名：" + PACKAGE_CONFIG_MODULE_NAME);
        // pc.setParent("com.baomidou.ant");
        pc.setParent(PACKAGE_CONFIG_PARENT);
        log.info("父包名：" + PACKAGE_CONFIG_PARENT);
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录，自定义目录用");
                if (fileType == FileType.MAPPER) {
                    // 已经生成 mapper 文件判断存在，不想重新生成返回 false
                    return !new File(filePath).exists();
                }
                // 允许生成模板文件
                return true;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);// 是否是RESTful的风格。
        // 公共父类
        // strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        // strategy.setSuperEntityColumns("id");
        // strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setInclude(STRATEGY_TABLE_NAME.split(","));
        log.info("表名称：" + STRATEGY_TABLE_NAME);
        strategy.setControllerMappingHyphenStyle(true);
        // strategy.setTablePrefix(pc.getModuleName() + "_"); // 设置表名前缀
        // liuyang 逻辑伤处字段名称
        // 配置上之后，如果发现表中有这个字段，在影射实体的时候会增加逻辑删除注解。
        // strategy.setLogicDeleteFieldName("deleted");
        // liuyang 自动填充策略
        /*
        List<TableFill> tableFills = new ArrayList<>();
        TableFill tfCreateTime = new TableFill("create_time", FieldFill.INSERT);// 表字段，填充策略
        TableFill tfModifiedTime = new TableFill("modified_time", FieldFill.UPDATE);
        TableFill tfCreateAccountId = new TableFill("create_account_id", FieldFill.INSERT);
        TableFill tfModifiedAccountId = new TableFill("modified_account_id", FieldFill.UPDATE);
        tableFills.add(tfCreateTime);
        tableFills.add(tfModifiedTime);
        tableFills.add(tfCreateAccountId);
        tableFills.add(tfModifiedAccountId);
        strategy.setTableFillList(tableFills);
        */

        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
