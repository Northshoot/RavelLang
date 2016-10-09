package ai.harmony.api.lang;

import ai.harmony.api.builder.FileObject;
import ai.harmony.api.platforms.ConcretePlatform;
import ai.harmony.ravel.primitives.Model;
import ai.harmony.ravel.primitives.Space;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static ai.harmony.api.Settings.BASE_TMPL_PATH;

/**
 * Class that is responsible for creating a particular language representation
 * <p>
 * Created by lauril on 10/6/16.
 */
public class CLang extends ConcreteLanguage{
    private static Logger LOGGER = Logger.getLogger(CLang.class.getName());
    public final static String BASE_LANG_TMPL_PATH = BASE_TMPL_PATH +"/lang/c/tmpl";
    Space mSpace;
    STGroup model_tmpl;

    public CLang() {
        super();
        model_tmpl = new STGroupFile(BASE_LANG_TMPL_PATH +"/model.stg");
    }



    @Override
    public List<FileObject> build(Space s){
        LOGGER.info("Building Space: " +s.mName);
        mSpace = s;
        createModels();
        createControllers();
        return mFileObjects;
    }

    private void createModels() {

        ST model_header = model_tmpl.getInstanceOf("models_header_file");
        model_header.add("space", mSpace);
        FileObject f = new FileObject();
        f.setPath(mSpace.getBuildPath());
        f.setFileName("model.h");
        f.setContent(model_header.render());
        mFileObjects.add(f);
        ST model_object = model_tmpl.getInstanceOf("models_obj_file");
        model_object.add("space", mSpace);
//        System.out.println(model_object.render());
        f = new FileObject();
        f.setPath(mSpace.getBuildPath());
        f.setFileName("model.c");
        f.setContent(model_object.render());
        mFileObjects.add(f);
        //create buffer files
        f = new FileObject();
        f.setPath(mSpace.getBuildPath() +"/api/");
        f.setFileName("ringbuf.c");
        f.setContent(getBufferObj());
        mFileObjects.add(f);
        f = new FileObject();
        f.setPath(mSpace.getBuildPath() +"/api/");
        f.setFileName("ringbuf.h");
        f.setContent(getBufferHeader());
        mFileObjects.add(f);

    }



    private void createControllers() {
    }

    private String getBufferObj() {
        String ret = "/*\n" +
                "   Generic High Performance Ring Buffer\n" +
                "\n" +
                "   Copyright (c) 2011 Christian Beier <dontmind@freeshell.org>\n" +
                "   All rights reserved.\n" +
                "\n" +
                "   Redistribution and use in source and binary forms, with or without\n" +
                "   modification, are permitted provided that the following conditions\n" +
                "   are met:\n" +
                "   1. Redistributions of source code must retain the above copyright\n" +
                "   notice, this list of conditions and the following disclaimer.\n" +
                "   2. Redistributions in binary form must reproduce the above copyright\n" +
                "   notice, this list of conditions and the following disclaimer in the\n" +
                "   documentation and/or other materials provided with the distribution.\n" +
                "   3. The name of the author may not be used to endorse or promote products\n" +
                "   derived from this software without specific prior written permission.\n" +
                "\n" +
                "   THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR\n" +
                "   IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES\n" +
                "   OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.\n" +
                "   IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,\n" +
                "   INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT\n" +
                "   NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,\n" +
                "   DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY\n" +
                "   THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n" +
                "   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF\n" +
                "   THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n" +
                "*/\n" +
                "\n" +
                "\n" +
                "#include <string.h>\n" +
                "#include \"ringbuf.h\"\n" +
                "\n" +
                "\n" +
                "ghpringbuf* ghpringbuf_create(size_t capacity, size_t item_size, int is_overwriting, void (*item_cleaner)(void*))\n" +
                "{\n" +
                "  ghpringbuf* b = calloc(1, sizeof(ghpringbuf));\n" +
                "  if(!b)\n" +
                "    return NULL;\n" +
                "  b->capacity = capacity;\n" +
                "  b->item_sz = item_size;\n" +
                "  b->is_overwriting = is_overwriting;\n" +
                "  b->clean_item = item_cleaner;\n" +
                "  b->items = calloc(capacity, item_size);\n" +
                "  if(!b->items)\n" +
                "    {\n" +
                "      free(b);\n" +
                "      return NULL;\n" +
                "    }\n" +
                "  return b;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "void ghpringbuf_destroy(ghpringbuf* b)\n" +
                "{\n" +
                "  if(!b)\n" +
                "    return;\n" +
                "\n" +
                "  if(b->clean_item)\n" +
                "    {\n" +
                "      size_t i, count = ghpringbuf_count(b); /* pop decrements count */\n" +
                "      for(i=0; i < count; ++i)\n" +
                "\tghpringbuf_pop(b);\n" +
                "    }\n" +
                "  free(b->items);\n" +
                "  free(b);\n" +
                "}\n" +
                "\n" +
                "\n" +
                "int ghpringbuf_put(ghpringbuf* b, void* item)\n" +
                "{\n" +
                "  b->lock = 1;\n" +
                "  if (b->count < b->capacity)\n" +
                "    {\n" +
                "      char* it = b->items;\n" +
                "      it += b->item_sz * b->iput;\n" +
                "      memcpy(it, item, b->item_sz);\n" +
                "      b->iput = (b->iput+1) == b->capacity ? 0 : b->iput+1; /* advance or wrap around */\n" +
                "      b->count++;\n" +
                "    }\n" +
                "  else\n" +
                "    {\n" +
                "      if(b->is_overwriting)\n" +
                "\t{\n" +
                "\t  char* it = b->items;\n" +
                "\t  it += b->item_sz * b->iput;\n" +
                "\n" +
                "\t  if(b->clean_item) /* clean out item that we will overwrite */\n" +
                "\t    b->clean_item(it);\n" +
                "\n" +
                "\t  memcpy(it, item, b->item_sz);\n" +
                "\t  b->iget = b->iput = (b->iput+1) == b->capacity ? 0 : b->iput+1; /* advance or wrap around */\n" +
                "\t}\n" +
                "      else\n" +
                "\t{\n" +
                "\t  b->lock = 0;\n" +
                "\t  return 0; /* buffer full */\n" +
                "\t}\n" +
                "    }\n" +
                "  b->lock = 0;\n" +
                "  return 1;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "int ghpringbuf_insert(ghpringbuf* b, size_t index, void* src)\n" +
                "{\n" +
                "  b->lock = 1;\n" +
                "  if (b->count > 0 && index < b->count)\n" +
                "    {\n" +
                "      size_t pos = b->iget + index;\n" +
                "      if(pos >= b->capacity)\n" +
                "\tpos -= b->capacity;\n" +
                "\n" +
                "      char* it = b->items;\n" +
                "      it += b->item_sz * pos;\n" +
                "      memcpy(it, src, b->item_sz);\n" +
                "      b->lock = 0;\n" +
                "      return 1;\n" +
                "    }\n" +
                "  b->lock = 0;\n" +
                "  return 0;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "void* ghpringbuf_at(ghpringbuf* b, size_t index)\n" +
                "{\n" +
                "  b->lock = 1;\n" +
                "  if (b->count > 0 && index < b->count)\n" +
                "    {\n" +
                "      size_t pos = b->iget + index;\n" +
                "      if(pos >= b->capacity)\n" +
                "\tpos -= b->capacity;\n" +
                "\n" +
                "      char* it = b->items;\n" +
                "      it += b->item_sz * pos;\n" +
                "      b->lock = 0;\n" +
                "      return it;\n" +
                "    }\n" +
                "  b->lock = 0;\n" +
                "  return 0;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "int ghpringbuf_pop(ghpringbuf* b)\n" +
                "{\n" +
                "  b->lock = 1;\n" +
                "  if (b->count > 0)\n" +
                "    {\n" +
                "      if(b->clean_item)\n" +
                "\t{\n" +
                "\t  char* it = b->items;\n" +
                "\t  it += b->item_sz * b->iget; /* go to iget index */\n" +
                "\t  if(b->clean_item) /* clean out item that we will abandon */\n" +
                "\t    b->clean_item(it);\n" +
                "\t}\n" +
                "\n" +
                "      b->iget = (b->iget+1) == b->capacity ? 0 : b->iget+1; /* advance or wrap around */\n" +
                "      b->count--;\n" +
                "      b->lock = 0;\n" +
                "      return 1;\n" +
                "    }\n" +
                "  b->lock = 0;\n" +
                "  return 0;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "size_t ghpringbuf_count(ghpringbuf* b)\n" +
                "{\n" +
                "  return b->count;\n" +
                "}\n" +
                "\n" +
                "\n";
        return ret;
    }
    private String getBufferHeader(){
        String ret = "";
        ret+="/*\n" +
                "   Generic High Performance Ring Buffer\n" +
                "\n" +
                "   Copyright (c) 2011 Christian Beier <dontmind@freeshell.org>\n" +
                "   All rights reserved.\n" +
                "\n" +
                "   Redistribution and use in source and binary forms, with or without\n" +
                "   modification, are permitted provided that the following conditions\n" +
                "   are met:\n" +
                "   1. Redistributions of source code must retain the above copyright\n" +
                "   notice, this list of conditions and the following disclaimer.\n" +
                "   2. Redistributions in binary form must reproduce the above copyright\n" +
                "   notice, this list of conditions and the following disclaimer in the\n" +
                "   documentation and/or other materials provided with the distribution.\n" +
                "   3. The name of the author may not be used to endorse or promote products\n" +
                "   derived from this software without specific prior written permission.\n" +
                "\n" +
                "   THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR\n" +
                "   IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES\n" +
                "   OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.\n" +
                "   IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,\n" +
                "   INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT\n" +
                "   NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,\n" +
                "   DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY\n" +
                "   THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n" +
                "   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF\n" +
                "   THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n" +
                "*/\n" +
                "\n" +
                "#ifndef GHPRINGBUF_H\n" +
                "#define GHPRINGBUF_H\n" +
                "\n" +
                "#include <stdlib.h>\n" +
                "\n" +
                "\n" +
                "/**\n" +
                "  the ringbuffer struct\n" +
                "*/\n" +
                "typedef struct _ghpringbuf {\n" +
                "  void* items;\n" +
                "  size_t iput;      /* index for next item to be put */\n" +
                "  size_t iget;      /* index for next item to be got */\n" +
                "  size_t item_sz;      /* size of one item */\n" +
                "  void (*clean_item)(void *item); /* item cleaner callback. set in case item contains pointers to other stuff. */\n" +
                "  size_t count;        /* number of items in buffer */\n" +
                "  size_t capacity;     /* max item count */\n" +
                "  int lock;            /* internal lock */\n" +
                "  int is_overwriting;  /* if this is an overwriting buffer or not */\n" +
                "  int flags;           /*< general purpose flags to mark buffer */\n" +
                "} ghpringbuf;\n" +
                "\n" +
                "\n" +
                "\n" +
                "/** creates a new empty ringbuffer of capacity capacity, item size item_size,\n" +
                "    if this is an overwriting buffer or not, and a item cleaner callback or NULL */\n" +
                "ghpringbuf* ghpringbuf_create(size_t capacity, size_t item_size, int is_overwriting, void (*item_cleaner)(void*));\n" +
                "\n" +
                "/** destroys a ghpringbuf, deallocating all internal data */\n" +
                "void ghpringbuf_destroy(ghpringbuf* b);\n" +
                "\n" +
                "/** copy item at item_ptr to end of ringbuffer. returns 1 on success, 0 if buffer full */\n" +
                "int ghpringbuf_put(ghpringbuf* b, void* item_ptr);\n" +
                "\n" +
                "/** insert item at index, will be copied from src. returns 1 on success, 0 if index out of bounds */\n" +
                "int ghpringbuf_insert(ghpringbuf* b, size_t index, void* src);\n" +
                "\n" +
                "/** access item at index, returns pointer to item on success, NULL if index out of bounds */\n" +
                "void* ghpringbuf_at(ghpringbuf* b, size_t index);\n" +
                "\n" +
                "/** remove first item. returns 1 on success, 0 if buffer empty */\n" +
                "int ghpringbuf_pop(ghpringbuf* b);\n" +
                "\n" +
                "/** get number of buffered items */\n" +
                "size_t ghpringbuf_count(ghpringbuf* b);\n" +
                "\n" +
                "\n" +
                "#endif\n";
        return ret;
    }
}
