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
      clearable="clearable">
    </el-date-picker>
  </div>
</template>

<script>

    export default {
        props: ["start", "end"],
        data() {
            return {
                pickValue: "",
            }
        }, watch: {
            pickValue() {
                if (this.pickValue && this.pickValue.length > 0) {
                    this.$emit("update:start", this.pickValue[0])
                } else
                    this.$emit("update:start", "")
                if (this.pickValue && this.pickValue.length > 1) {
                    this.$emit("update:end", this.pickValue[1])
                } else {
                    this.$emit("update:end", "")
                }
            }
        }
    }
</script>
