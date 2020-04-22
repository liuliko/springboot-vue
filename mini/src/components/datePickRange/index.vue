<template>
  <div style="display: inline-block">
    <el-date-picker
      size="mini"
      v-model="pickValue"
      type="daterange"
      align="left"
      unlink-panels
      value-format="yyyyMMdd"
      range-separator="至"
      start-placeholder="开始日期"
      end-placeholder="结束日期"
      @change="picked"
      :clearable="clearable"
      :picker-options="pickerOptions">
    </el-date-picker>
  </div>
</template>
<script>
  export default {
    model: {
      prop: "value",
      event: "pick"
    },
    props: {
      value: {
        default: []
      },
      clearable:{
        type:Boolean,
        default:false
      }
    },
    mounted() {

    },
    watch:{
      pickValue:{
        handler(val,oldVal){
          this.$emit('pick', val)
        },
        deep:true
      }
    },
    methods: {
      picked(value) {
        // this.$emit('pick', value)
      },
      pickLastWeek(picker) {
        const start = this.$moment().week(this.$moment().week() - 1).startOf('week').format("YYYYMMDD");
        const end = this.$moment().week(this.$moment().week() - 1).endOf('week').format("YYYYMMDD");
        picker.$emit('pick', [start, end]);

      },
      pickCurrentMonth(picker) {
        const start = this.$moment().month(this.$moment().month()).startOf('month').format("YYYYMMDD");
        const end = this.$moment().format("YYYYMMDD");
        if (picker)
          picker.$emit('pick', [start, end]);
        else
          this.pickValue = [start, end]

      },
      pickLastMonth(picker) {
        const start = this.$moment().month(this.$moment().month() - 1).startOf('month').format("YYYYMMDD");
        const end = this.$moment().month(this.$moment().month() - 1).endOf('month').format("YYYYMMDD");
        if (picker)
          picker.$emit('pick', [start, end]);
        else
          this.pickValue = [start, end]

      },
      pickLastQuarter(picker) {
        const start = this.$moment().quarter(this.$moment().quarter() - 1).startOf('quarter').format("YYYYMMDD");
        const end = this.$moment().quarter(this.$moment().quarter() - 1).endOf('quarter').format("YYYYMMDD");
        picker.$emit('pick', [start, end]);

      },
    },
    data() {
      return {
        pickValue: "",
        pickerOptions: {
          shortcuts: [
            {
              text: '当前月',
              onClick: this.pickCurrentMonth
            },
            {
              text: '上周',
              onClick: this.pickLastWeek
            }, {
              text: '上个月',
              onClick: this.pickLastMonth
            }, {
              text: '上季度',
              onClick: this.pickLastQuarter
            }]
        },
      }
    }
  }
</script>
