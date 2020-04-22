<template>
  <div class="table">
    <el-table
      :data="data"
      border
      fit
      max-height="maxHeight"
      v-loading="listLoading"
      element-loading-text="正在加载...."
      :summary-method="summary"
      :show-summary="showSummary"
      @cell-click="tableCellClick"
      :row-class-name="rowClassName"
      highlight-current-row
    >

      <el-table-column
        type="index"
        width="50"
      >
      </el-table-column>

      <el-table-column show-overflow-tooltip v-for="col in columns" :prop="col.prop" :width="col.width"
                       :sortable="col.sortable" :sort-method="col.sortMethod"
                       :label="col.label" :formatter="col.formatter" :align="col.align">
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

          <slot v-if="col.slotName!==undefined" :name="col.slotName" :row="scope.row" :index="scope.$index"></slot>
          <span v-if="col.slotName===undefined && (col.popover===undefined||!isFlag)" :title="col.formatter(scope.row)">
            {{(col.label.indexOf("时间")>-1 ||col.label.indexOf("日期")>-1) ?tableFormatTime(scope.row,col.formatter) :col.formatter(scope.row) }}
          </span>
        </template>
      </el-table-column>

      <el-table-column label="操作" v-if="buttons !==undefined && isFlag" width="200">
        <template slot-scope="scope" class="operator">
          <el-button v-for="btn in buttons" :type="btn.type" size="mini"
                     @click="handleBtn(btn,scope.$index, scope.row)">{{btn.label}}
          </el-button>
        </template>
      </el-table-column>

    </el-table>
  </div>
</template>
<script>
  import {formatTime} from '@/api/StringUtil'
  import MyPopover from "../Popover/my-popover";

  export default {
    components: {MyPopover},
    props: {
      columns: {
        type: Array
      },
      maxHeight:{
        type:Number,
        default:undefined
      },
      data: {
        type: Array
      },
      refresh: {
        type: Function
      },
      popoverSource: {
        type: Array
      },
      buttons: {
        type: Array
      },
      showSummary: {
        type: Boolean,
        default: false
      },
      isFlag: {
        type: Boolean,
        default: true
      },
      cellClick: {
        type: Function
      },
      rowClass: {
        type: Function
      }
    },
    data() {
      return {
        listLoading: false
      }
    }, methods: {
      tableCellClick: function (row, column, cell, event) {
        if (this.cellClick) {
          this.cellClick(row, column)
        }
      },
      rowClassName: function ({row, column, rowIndex, columnIndex}) {
        if (this.rowClass) {
          return this.rowClass(row);
        }
        return "";
      },
      tableFormatTime(row, format) {
        let value = format(row)
        return formatTime(value)
      },
      handleBtn: function (btn, index, row) {
        btn.click(index, row)
      },
      summary: function (param) {
        const {data} = param;
        const columns = this.columns;

        const sums = ["总计"];
        columns.forEach((column, index) => {


          if (data) {
            const values = data.map(item => {
              if (column.numberMethod) {
                return Number(column.numberMethod(item));
              }
              return Number(item[column.prop])
            });
            if (!values.every(value => isNaN(value)) &&
              column.prop !== 'commodityLineNo' && column.prop !== 'commodityCreateDate' &&
              column.prop !== 'commodityCase') {
              sums[index + 1] = values.reduce((prev, curr) => {
                const value = Number(curr);
                if (!isNaN(value)) {
                  return prev + curr;
                }{
                  return prev;
                }
              }, 0);
              if (column.label === '售价' || column.label === '采购价格' ||column.label === '单价') {
                sums[index + 1] = ''
              }

              if (column.label === '折扣') {
                sums[index + 1] = sums[index+1].toFixed(2)
              }

              if (column.prop === 'totalAmount' ) {
                sums[index + 1] = sums[index+1].toFixed(2) + ''
              }

            } else {
              sums[index + 1] = '';
            }
          }
        });
        return sums;
      }
    }, mounted() {
    }
  }

</script>

