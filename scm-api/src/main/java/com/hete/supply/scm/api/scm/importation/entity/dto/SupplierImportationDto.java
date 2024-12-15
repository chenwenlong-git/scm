package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SupplierExport;
import com.hete.supply.scm.api.scm.entity.enums.SupplierGrade;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInvoicing;
import com.hete.supply.scm.api.scm.entity.enums.SupplierType;
import com.hete.support.api.entity.dto.BaseImportationRowDto;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
public class SupplierImportationDto {
    /**
     * 导入数据
     */
    private List<ImportationDetail> detailList;

    @Data
    public static class ImportationDetail extends BaseImportationRowDto {

        @NotBlank(message = "供应商代码不能为空")
        @Length(max = 32, message = "供应商代码长度不能超过32个字符")
        private String supplierCode;


        @NotBlank(message = "供应商名称不能为空")
        @Length(max = 32, message = "供应商名称长度不能超过32个字符")
        private String supplierName;

        @NotNull(message = "供应商类型不能为空")
        private SupplierType supplierType;

        /**
         * 供应商等级
         */
        private SupplierGrade supplierGrade;

        @NotNull(message = "入驻时间不能为空")
        private LocalDateTime joinTime;

        @NotBlank(message = "开发采购员不能为空")
        private String devUsername;

        @NotBlank(message = "跟单采购员不能为空")
        private String followUsername;


        @NotNull(message = "产能不能为空")
        private Integer capacity;

        @NotNull(message = "物流时效不能为空")
        private Integer logisticsAging;

        /**
         * 进出口资质
         */
        private SupplierExport supplierExport;

        /**
         * 开票资质
         */
        private SupplierInvoicing supplierInvoicing;


        /**
         * 税点
         */
        private BigDecimal taxPoint;

        /**
         * 社会信用代码
         */
        @Length(max = 32, message = "社会信用代码长度不能超过32个字符")
        private String creditCode;

        /**
         * 公司名称
         */
        @Length(max = 32, message = "公司名称长度不能超过32个字符")
        private String corporateName;

        /**
         * 法定代表人
         */
        @Length(max = 32, message = "法定代表人长度不能超过32个字符")
        private String legalPerson;

        /**
         * 营业期限开始
         */
        private LocalDateTime businessTimeStart;

        /**
         * 营业期限结束
         */
        private LocalDateTime businessTimeEnd;


        /**
         * 国家
         */
        @Length(max = 32, message = "国家长度不能超过32个字符")
        private String country;


        /**
         * 省/州
         */
        @Length(max = 32, message = "省/州长度不能超过32个字符")
        private String province;

        /**
         * 城市
         */
        @Length(max = 32, message = "城市长度不能超过32个字符")
        private String city;

        /**
         * 详细地址【营业执照】
         */
        @Length(max = 255, message = "详细地址长度不能超过255个字符")
        private String address;


        /**
         * 收货地址国家
         */
        @Length(max = 32, message = "收货地址国家不能超过32个字符")
        private String shipToCountry;


        @Length(max = 32, message = "收货地址省份/州不能超过32个字符")
        private String shipToProvince;


        @Length(max = 32, message = "收货地址城市不能超过32个字符")
        private String shipToCity;


        @Length(max = 255, message = "收货地址详细地址不能超过255个字符")
        private String shipToAddress;


        @Length(max = 255, message = "备注不能超过255个字符")
        private String remarks;

        @Length(max = 32, message = "开户行不能超过32个字符")
        private String accountBank;

        @Length(max = 255, message = "网点不能超过255个字符")
        private String networkAddress;

        @Length(max = 32, message = "开户人不能超过32个字符")
        private String registrationPeople;

        @Length(max = 32, message = "账号不能超过32个字符")
        private String account;

        @Length(max = 32, message = "联系人姓名不能超过32个字符")
        private String name;

        @Length(max = 32, message = "职位不能超过32个字符")
        private String position;

        @Length(max = 11, message = "联系人电话不能超过11个字符")
        private String phone;

        @Length(max = 255, message = "联系人备注不能超过255个字符")
        private String supplierContactRemarks;

        @NotBlank(message = "供应商别称不能为空")
        @Length(max = 32, message = "供应商别称长度不能超过32个字符")
        private String supplierAlias;

    }
}
