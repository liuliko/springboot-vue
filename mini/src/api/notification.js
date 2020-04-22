import {Notification} from 'element-ui'

export function createNotification(text, type, title) {
  return Notification({
    title: title ? title : '提示',
    type: type,
    message: text,
    duration: 1500,
    offset: 20,
    position: 'bottom-right',
    customClass: "notifcation-" + type
  })
}
