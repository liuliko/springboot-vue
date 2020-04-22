<template>
  <div>
    <el-table
      size="mini"
      ref="currentTable"
      style="width: 100%"
      v-loading="listLoading"
      :data="pageParam.aaData"
      element-loading-text="正在加载...."
      stripe
      aria-checked
      @selection-change="handleSelectionChange"
      fit border="border"
      :cell-class-name="cellclassName"
      highlight-current-row :show-summary="summaryIndex !=-1"
      :summary-method="summary"
      :index="index">

      <el-table-column width="30" v-if="needSelect"
                       type="selection">
      </el-table-column>


      <el-table-column type="expand" v-if="expandSlot">
        <template slot-scope="scope">
          <slot name="expandSlot" :row="scope.row"></slot>
        </template>
      </el-table-column>


      <el-table-column
        type="index"
        :index="index" show-overflow-tooltip
      >
      </el-table-column>

      <el-table-column v-for="col in colums" :label="col.label"  :align="col.align"
                       show-overflow-tooltip>
        <template slot-scope="scope">

          <my-popover v-if="col.popover !== undefined && isFlag"
                      :url="col.popover.url"
                      :enable-minus="col.popover.enableMinus"
                      :id="scope.row.id"
                      :rowValue="scope.row[col.prop]"
                      :prop-type="col.popover.type"
                      :prop="col.popover.prop"
                      :callback="refresh"
                      :source="popoverSource"
                      :validate="col.popover.validate"
          />

          <slot v-if="col.slotName!=undefined" :name="col.slotName" :row="scope.row" :index="scope.$index"></slot>
          <span v-if="col.slotName==undefined && (col.popover===undefined||!isFlag)" :title="col.formatter(scope.row)">{{ col.label.indexOf("时间")>-1?tableFormatTime(scope.row,col.formatter) :col.formatter(scope.row) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" v-if="buttons.length==1" width="100">
        <template slot-scope="scope" class="operator">
          <el-button v-for="btn in buttons" :type="btn.type" size="mini"
                     @click="handleBtn(btn,scope.$index, scope.row)">{{btn.label}}
          </el-button>
        </template>
      </el-table-column>

      <el-table-column label="操作" v-if="buttons.length>1" width="150">
        <template slot-scope="scope" class="operator">
          <el-dropdown size="mini" split-button type="primary" @click="handleBtn(buttons[0],scope.$index, scope.row)"
                       trigger="click">
            {{buttons[0].label}}
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-for="btn in buttons">
                <div @click="handleBtn(btn,scope.$index, scope.row)"><i
                  :class="btn.label == '修改' ? 'el-icon-edit':  (btn.label == '删除' ? 'el-icon-delete' : btn.icon)"></i>{{btn.label}}
                </div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>


        </template>
      </el-table-column>

    </el-table>

    <div style="width: 100%;height: 10px"></div>

    <el-pagination
      small
      :current-page="currentPage"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageParam.iDisplayLength"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pageParam.iTotalRecords"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>
<script>
  import request from '@/utils/request'
  import {formatTime} from '@/api/StringUtil'

  import MyPopover from "../Popover/my-popover";

  export default {
    components: {MyPopover},
    props: {
      fetchUrl: String,
      expendBtn: {
        type: Boolean,
        defalut: false
      },
      isFlag: {
        type: Boolean,
        default: true
      },
      popoverSource: {
        type: Array
      },
      summaryIndex: {
        type:[Array,Number],
        default: -1
      },
      border: {
        type: Boolean,
        default: false
      },
      needSelect: {
        type: Boolean,
        default: true
      },
      expandSlot: {
        type: Boolean,
        default: false
      },
      btnFixed: {
        type: Boolean,
        default: true
      },
      autoGetData: {
        type: Boolean,
        default: true
      },
      colums: {
        type: Array,
        default: [{}]
      },
      buttons: {
        type: Array,
        default: function () {
          return []
        }
      },
      params: {
        type: Object,
        default: {}
      }
    },
    data() {
      return {
        currentPage: 1,
        pageParam: [],
        listLoading: false,
        multipleSelection: []
      }
    }, methods: {
      refresh(){
        this.fetchData();
      },
      summary() {
        let colSize = 1;
        colSize += this.needSelect ? 1 : 0
        colSize += this.buttons.length > 0 ? 1 : 0
        colSize += this.expandSlot ? 1 : 0
        colSize += this.colums.length;
        let result = [];



        if (!this.pageParam.message) {
          return result;
        }

        let messageArray = this.pageParam.message.split(",")
        let indexArray = Array.isArray(this.summaryIndex)  ? this.summaryIndex : [this.summaryIndex]
        let j = 0


        for (let i = 0; i < colSize; i++) {
          if (indexArray.includes(i)){

            result[i] = messageArray[j++]
          }
          else if (i == (1 + (this.needSelect ? 1 : 0) + (this.expandSlot ? 1 : 0)))
            result[i] = "合计"
          else
            result[i] = ""
        }
        return result
      },
      tableFormatTime(row, format) {
        let value = format(row)
        return formatTime(value)
      },
      handleBtn: function (btn, index, row) {
        btn.click(row, index)
      },
      fetchData: function () {
        var data = {}
        for (var param in this.pageParam) {
          if (param !== 'aaData') {
            data[param] = this.pageParam[param]
          }
        }
        for (var item in this.params) {
          if (this.params[item] !== '') {
            let value = this.params[item]
            if (typeof value == 'string') {
              value = value.trim()
            }
            data[item] = value
          }
        }
        this.listLoading = true
        request({
          url: this.fetchUrl,
          method: 'post',
          params: data
        }).then(({data, msg}) => {

          if (data.aaData.length == 0 && data.iTotalRecords > 0) {
            /**
             *  在非第一页的情况下，添加新的查询条件进行查询，很可能存在这种情况
             *  查询的总记录数大于0，但是查询结果是空的。因为查询的当前页码得数据。
             *  这里发现这种情况，直接跳到第一页
             */
            this.handleCurrentChange(1)
          } else {
            this.pageParam = data
          }


        }).catch(() => {
        }).finally(_ => {
          this.listLoading = false
        })
      },
      handleSizeChange(val) {
        this.pageParam.iDisplayLength = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.currentPage = val
        this.pageParam.iDisplayStart = (val - 1) * (this.pageParam.iDisplayLength ? this.pageParam.iDisplayLength : 0)
        this.fetchData()
      },
      index(index) {
        return this.pageParam.iDisplayStart + index + 1
      }, handleSelectionChange(val) {
        this.multipleSelection = val
      }, getSelectedData() {
        return this.multipleSelection
      }, cellclassName() {
        return 'nowrap ceter'
      }
    }, mounted() {
      if (this.autoGetData)
        this.fetchData()
    }
  }

</script>
