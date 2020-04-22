import { Loading } from 'element-ui'

/**
 * 创建loading 需要手动调用close方法
 * @param text
 * @returns {ElLoadingComponent}
 */
export function createLoading(text) {
  if (!text) {
    text = '正在加载...'
  }
  return Loading.service({
    text:text
  });
}
