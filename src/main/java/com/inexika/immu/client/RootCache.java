package com.inexika.immu.client;

import com.codenotary.immudb.schema.Schema;
import java.io.*;

public class RootCache {
    private final String ROOT_FN = ".root";

    public Schema.Root get() throws IOException {
        File targetFile = new File(ROOT_FN);
        Schema.Root root = Schema.Root.parseFrom(new FileInputStream(targetFile));

        return root;
    }

    public void set(Schema.Root root) throws IOException{
        File file = new File(ROOT_FN);

        if (!file.exists()) {
            file.createNewFile();
        }

        OutputStream os = new FileOutputStream(file);
        root.writeTo(os);
        os.flush();
        os.close();
    }
}
