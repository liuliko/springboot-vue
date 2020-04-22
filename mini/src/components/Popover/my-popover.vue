<template>
  <div>
    <template>
      <el-popover
        placement="top"
        width="200"
        trigger="click"
        v-model="isShow"
        @show="showPov()"
        @hide="hidePov()"
      >
        <template v-if="propType === 'text'">
          <el-input clearable type="text" autocomplete v-model="inChangeMsg" @keyup.enter.native="save"></el-input>
        </template>

        <template v-if="propType === 'number'">
          <el-input type="number" autocomplete v-model="inChangeMsg" @keyup.enter.native="save"></el-input>
        </template>

        <template v-if="propType === 'textarea'">
          <el-input clearable type="textarea" autocomplete v-model="inChangeMsg"></el-input>
        </template>

        <template v-if="propType === 'money'">
          <el-amount-input autocomplete v-model="inChangeMsg" @keyup.enter.native="save"></el-amount-input>
        </template>

        <template v-if="propType === 'select'">
          <el-select clearable filterable v-model="inChangeMsg" autocomplete="off">
            <el-option v-for="item in source" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </template>

        <template v-if="propType === 'date'">
          <el-date-picker
            @change="dateChange"
            clearable
            style="width: 180px;"
            v-model="inChangeMsg"
            type="date"
            format="yyyy-MM-dd"
            value-format="yyyyMMdd"
          >
          </el-date-picker>
        </template>

        <div style="text-align: right; margin-top: 5px;">
          <el-button icon="el-icon-check" type="primary" size="mini" @click="save()"></el-button>
          <el-button icon="el-icon-close" size="mini" @click="isShow = false"></el-button>
        </div>

        <span v-if="propType === 'money'" slot="reference" style="color: #428bca;cursor: pointer">
            {{ rowValue||rowValue==0?this.$bigjs(rowValue).div(100).toFixed(2):"Empty" }}</span>

        <span v-else-if="propType === 'date'" slot="reference" style="color: #428bca;cursor: pointer">
            {{ rowValue?format(rowValue):'Empty' }}</span>

        <span v-else slot="reference" style="color: #428bca;cursor: pointer">
            {{ rowValue||rowValue==0?rowValue:'Empty' }}</span>
      </el-popover>
    </template>
  </div>
</template>

<script>
  import request from '@/utils/request'
  import {createNotification} from '@/api/notification'
  import {formatTime} from '@/api/StringUtil'

  export default {
    name: "my-popover",
    data() {
      return {
        inChangeMsg: "",
        isShow: false,
        params: {},
        date: ""
      }
    },
    props: {
      rowValue: String | Number,
      prop: String,
      id: String,
      url: String,
      callback: Function,
      propType: {
        type: String,
        default: "number",
      },
      enableMinus: {
        type: Boolean,
        default: false
      },
      source: Function | Array,
      validate: {
        type: Function,
        default: function () {
          if (this.propType === 'number') {
            if (!/^\-?\d+$/.test(this.inChangeMsg)) {
              this.$message({
                message: '请输入正确的数字',
                type: 'warning'
              });
              return false;
            }

            if (this.inChangeMsg <= 0 && !this.enableMinus) {
              this.$message({
                message: '请输入正整数',
                type: 'warning'
              });
              return false;
            }

            if (this.inChangeMsg > 99999999) {
              this.$message({
                message: '数量最大只能为八位正整数',
                type: 'warning'
              });
              return false;
            }
            return true;
          }
          return true;
        }
      }
    },
    methods: {
      dateChange: function (value) {
        this.date = value;
        this.isShow = true;
      },
      format: function (value) {
        return formatTime(value);
      },
      save: function () {
        //值为原值和空值不进行数据库提交
        if (this.rowValue !== this.inChangeMsg && this.inChangeMsg) {
          if (!this.validate(this.inChangeMsg)) {
            return;
          }

          this.params.id = this.id;
          this.params[this.prop] = this.inChangeMsg;
          request({
            url: this.url,
            method: 'post',
            params: this.params
          }).then(data => {
            //操作成功和失败返回的是数字 提示是一段文字
            if (data.data !== -1 && !isNaN(data.data)) {
              if (this.callback) {
                this.callback();
              }
            } else if (data.data === -1) {
              createNotification('保存失败', 'error')
              console.log(data)
            } else {
              createNotification(data.data, 'warning')
            }
          }).catch(() => {
            this.inChangeMsg = this.rowValue;
          })
        }
        this.isShow = false
      },
      showPov: function () {
        this.inChangeMsg = this.rowValue
        if (this.date) {
          this.inChangeMsg = this.date
        }
      }, hidePov: function () {
        this.date = ''
      }
    }, mounted() {
    }
  }
</script>

<style scoped>

</style>
