var month_split = "-"
var month_time_split = " "
var time_split = ":"

export function formatTime(time) {
  if (!time)
    return ""

  if (time.length == 14) {
    return time.substring(0, 4) + month_split
      + time.substring(4, 6) + month_split
      + time.substring(6, 8) + month_time_split
      + time.substring(8, 10) + time_split
      + time.substring(10, 12) + time_split
      + time.substring(12, 14)
  }
  if (time.length == 8) {
    return time.substring(0, 4) + month_split
      + time.substring(4, 6) + month_split
      + time.substring(6, 8)
  }

  if (time.length == 6) {
    return time.substring(0, 2) + time_split
      + time.substring(2, 4) + time_split
      + time.substring(4, 6)
  }

  return time;
}

export function formatOrderDetailTable(row) {
  var tableData = [];

  if (row.createManName)
    tableData.push({
      type: "创建人", name: row.createManName, time: formatTime(row.createTime)
    })

  if (row.handleManName)
    tableData.push({
      type: "经手人", name: row.handleManName, time: "-"
    })

  if (row.passportManName)
    tableData.push({
      type: "验收人", name: row.passportManName, time: "-"
    })

  if (row.financeVerifyName)
    tableData.push({
      type: "财务审核", name: row.financeVerifyName, time: formatTime(row.financeVerifyTime)
    })

  if (row.manangerVerifyName)
    tableData.push({
      type: "经理审核", name: row.manangerVerifyName, time: formatTime(row.manangerVerifyTime)
    })

  return tableData;
}


