/**
 * 数组过滤器 例如：{{CHARGE | optionKV(section.charge)}}  CHARGE代表list  section.charge代表key
 * @param list 例如[{key: "C", value: "收费"}, {key: "F", value: "免费"}]
 * @param key "C"
 * @returns {string} "收费"
 */
let optionKV = (list, key) => {
    if (!list || !key) {
        return "";
    } else {
        let result = "";
        for (let i = 0; i < list.length; i++) {
            if (key === list[i]["key"]) {
                result = list[i]["value"];
            }
        }
        return result;
    }
};

export default {
    optionKV
}