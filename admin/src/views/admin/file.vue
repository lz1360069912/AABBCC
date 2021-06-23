<template>
  <div>
    <p>
      <button v-on:click="add()" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-edit"></i>
        新增
      </button>
      &nbsp;
      <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh"></i>
        刷新
      </button>
    </p>

    <pagination ref="pagination" v-bind:list="list"></pagination>

    <table id="simple-table" class="table  table-bordered table-hover">
      <thead>
      <tr>
                <th>id</th>
        <th>相对路径</th>
        <th>文件名</th>
        <th>后缀</th>
        <th>大小</th>
        <th>用途</th>
        <th>操作</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="file in files">
        <td>{{ file.id }}</td>
        <td>{{ file.path }}</td>
        <td>{{ file.name }}</td>
        <td>{{ file.suffix }}</td>
        <td>{{ file.size }}</td>
        <td>{{ FILE_USE | optionKV(file.use) }}</td>
        <td>
          <div class="btn-group">
            <button v-on:click="edit(file)" class="btn btn-white btn-xs btn-info btn-round">
              编辑
            </button>&nbsp;
            <button v-on:click="del(file.id)" class="btn btn-white btn-xs btn-warning btn-round">
              删除
            </button>
          </div>
        </td>
      </tr>
      </tbody>
    </table>

    <div id="form-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">表单</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label for="path" class="col-sm-2 control-label">相对路径</label>
                <div class="col-sm-10">
                  <input v-model="file.path" type="text" class="form-control" id="path" placeholder="相对路径">
                </div>
              </div>
              <div class="form-group">
                <label for="name" class="col-sm-2 control-label">文件名</label>
                <div class="col-sm-10">
                  <input v-model="file.name" type="text" class="form-control" id="name" placeholder="文件名">
                </div>
              </div>
              <div class="form-group">
                <label for="suffix" class="col-sm-2 control-label">后缀</label>
                <div class="col-sm-10">
                  <input v-model="file.suffix" type="text" class="form-control" id="suffix" placeholder="后缀">
                </div>
              </div>
              <div class="form-group">
                <label for="size" class="col-sm-2 control-label">大小</label>
                <div class="col-sm-10">
                  <input v-model="file.size" type="text" class="form-control" id="size" placeholder="大小">
                </div>
              </div>
              <div class="form-group">
                <label for="use" class="col-sm-2 control-label">用途</label>
                <div class="col-sm-10">
                  <select v-model="file.use" class="form-control" id="use">
                    <option v-for="o in FILE_USE" v-bind:value="o.key">{{ o.value }}</option>
                  </select>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button v-on:click="save()" type="button" class="btn btn-primary">保存</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
  </div>

</template>

<script>
import Pagination from "../../components/pagination";

export default {
  components: {Pagination},
  name: "file-file",
  data: function () {
    return {
      file: {}, // 用于绑定form表单的数据
      files: [],
      FILE_USE: FILE_USE,
    }
  },
  mounted: function () {
    // sidebar激活样式方法一
    // this.$parent.activeSidebar("file-file-sidebar");
    let _this = this;
    _this.$refs.pagination.size = 5;
    _this.list(1);
  },
  methods: {
    /**
     * 点击编辑
     * @param file
     */
    edit(file) {
      let _this = this;
      _this.file = $.extend({}, file); // 列表数据复制一份赋值给表单file，防止修改表单时把列表也修改
      $("#form-modal").modal("show");//hide
    },
    /**
     * 点击新增
     */
    add() {
      let _this = this;
      //.modal里的modal是内置的方法,用于弹出或关闭模态框
      _this.file = {};
      $("#form-modal").modal("show");//hide
    },
    /**
     * 列表查询
     * @param page
     */
    list(page) {
      let _this = this;
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/file/admin/file/list", {
        page: page,
        size: _this.$refs.pagination.size
      }).then((response) => {
        Loading.hide();
        let resp = response.data;
        _this.files = resp.content.list;
        _this.$refs.pagination.render(page, resp.content.total);
      });
    },
    /**
     * 点击保存
     */
    save() {
      let _this = this;

      // 保存校验
      if (1 != 1
          || !Validator.require(_this.file.path, "相对路径")
          || !Validator.length(_this.file.path, "相对路径", 1, 100)
          || !Validator.length(_this.file.name, "文件名", 1, 100)
          || !Validator.length(_this.file.suffix, "后缀", 1, 10)
      ) {
        return;
      }
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/file/admin/file/save", _this.file).then((response) => {
        Loading.hide();
        let resp = response.data;
        if (resp.success) {
          $("#form-modal").modal("hide");
          _this.list(1);
          Toast.success("保存成功");
        } else {
          Toast.warning(resp.message);
        }
      });
    },
    /**
     * 点击删除
     * @param id
     */
    del(id) {
      let _this = this;
      Confirm.show("确认删除？", "删除后不可恢复，确认删除!", function () {
        _this.$ajax.delete(process.env.VUE_APP_SERVER + "/file/admin/file/delete/" + id).then((response) => {
          let resp = response.data;
          if (resp.success) {
            _this.list(1);
            Toast.success("删除成功");
          }
        })
      });
    }
  }
}
</script>