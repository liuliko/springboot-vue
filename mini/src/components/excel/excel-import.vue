<template>
  <div>
    <el-dialog v-el-drag-dialog :close-on-click-modal="false" width="50%" :title="title"
               :visible.sync="dialogFormVisible" @close="refresh">

      <div style="float: right;width:20%" v-for="item in excel_modals">
        <el-button size="mini" type="warning" plain @click="excel(item)">{{item.name}}</el-button>
      </div>
      <div style="width: 70%">
        <slot v-if="slotName!==undefined" :name="slotName"></slot>
      </div>
      <el-row>
        <el-col :span="13" style="padding: 20px">
          <el-upload
            :on-error="error"
            ref="upload"
            accept=".xls,.xlsx"
            :on-success="success"
            :auto-upload="false"
            class="upload-demo"
            drag
            :data="params"
            :action="actionUrl"
            :limit="limit"
            :on-exceed="handleExceed">
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">只能上传 .xls .xlsx 文件</div>

          </el-upload>
        </el-col>

        <el-col :span="11">
          <div style="margin-top: 15px;">{{text}}：</div>
          <div style="height: 278px;
            overflow-y: auto;">
            <div style="margin-top: 15px;" v-for="item in errorContent">
              <label v-html="item"></label>
            </div>
          </div>
        </el-col>
      </el-row>

      <div slot="footer" class="dialog-footer">
        <el-button type="danger" @click="clear">清空结果</el-button>
        <el-button v-if="preview" type="success" @click="submitUpload(0)">预 览</el-button>
        <el-button :disabled="flag" type="primary" @click="submitUpload(1)">确 定</el-button>
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import request from '@/utils/request'
  import {createLoading} from '@/api/loading'
  import {initCommonWebSocket} from '@/utils/index'
  import {getRootPath} from '@/utils/index'

  export default {
    name: "excel-import",
    data() {
      return {
        errorContent: [],
        dialogFormVisible: false,
        limit: 1,
        actionUrl: '',
        flag: false,
        text: '导入结果'
      }
    },
    props: {
      title: String,
      webSocketPath: String,
      excel_modals: Array,
      preview: Boolean,
      uploadUrl: String,
      slotName: String,
      params: Object,
      refresh: {
        type: Function,
        default: function () {

        }
      },
      check: {
        type: Function,
        default: function () {
          return true;
        }
      }
    }, methods: {
      createDialog: function () {
        //在提交路径前拼接上根路径
        this.actionUrl = getRootPath() + this.uploadUrl;
        this.dialogFormVisible = true;
      },
      excel: function (target) {

        let params = {}
        for (var item in this.params) {
          if (this.params[item] !== '') {
            let value = this.params[item]
            if (typeof value == 'string') {
              value = value.trim()
            }
            params[item] = value
          }
        }
        let loading = createLoading()
        request({
          url: target.path,
          method: 'POST',
          responseType: 'blob',
          onDownloadProgress: function (progressEvent) {

          },
          params: params
        }).then((blob) => {
          this.downFile(blob, target.name + '.xls')
          loading.close()
        }).catch((e) => {
          loading.close()
          console.log('error', e)
        })
      },
      downFile: function (blob, fileName) {
        // 非IE下载
        if ('download' in document.createElement('a')) {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob) // 创建下载的链接
          link.download = fileName // 下载后文件名
          link.style.display = 'none'
          document.body.appendChild(link)
          link.click() // 点击下载
          window.URL.revokeObjectURL(link.href) // 释放掉blob对象
          document.body.removeChild(link) // 下载完成移除元素
        } else {
          // IE10+下载
          window.navigator.msSaveBlob(blob, fileName)
        }
      },
      initWebSocket: function () {
        initCommonWebSocket(this.webSocketPath, (data) => {
          data = JSON.parse(data);
          if (data.message.indexOf('成功') !== -1) {
            this.errorContent.push('<span style="color:green;">' + data.message + '</span>');
          } else {
            this.errorContent.push('<span style="color:red;">' + data.message + '</span>');
          }
        })
      },
      submitUpload(type) {
        this.$refs.upload.data.type = type;
        this.errorContent = []
        if (this.check()) {
          if (this.$refs.upload.uploadFiles.length === 0) {
            this.$message({
              message: '请选择上传文件',
              type: 'warning'
            });
            return;
          }
          this.$refs.upload.uploadFiles[0].status = 'ready'
          this.$refs.upload.submit();
          if(type === 0){
            this.text = '预览结果'
          }else{
            this.flag = true;
            this.text = '导入结果'
          }
        }
      },
      success: function (response, file, fileList) {
        if (response.data) {
          this.$message({
            message: response.data,
            type: 'error'
          });
        }
      },
      error: function (error, file, fileList) {
        this.$message.error(`导入文件失败`);
        this.flag = false;
      },
      clear: function () {
        this.flag = false;
        this.text = '导入结果'
        this.errorContent = []
        this.$refs.upload.clearFiles();
      },
      cancel: function () {
        this.flag = false;
        this.dialogFormVisible = false
        this.errorContent = []
        this.text = '导入结果'
        this.$refs.upload.clearFiles();
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件`);
      }
    }, mounted() {
      this.initWebSocket();
    }
  }
</script>

<style scoped>

</style>
