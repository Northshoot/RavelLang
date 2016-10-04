package ai.harmony.api.platforms;

import ai.harmony.api.builder.FileObject;
import ai.harmony.api.lang.c.Declaration;
import ai.harmony.api.lang.c.FuncDeclaration;

import java.util.List;

/**
 * Created by lauril on 9/21/16.
 */
public interface RavelObjectInterface {
    /**
     * if object is build separately it has file name
     * @return
     */
    public String getFileName();

    public String getHeaderDefName();

    /**
     * if C object it has header file name
     * @return null or header file name
     */
    public String getHeaderFileName();
    /**
     * is object build separately
     * @return
     */

    public List<Declaration> getImports();
    public List<Declaration> getDefines();
    public List<FuncDeclaration> getFuncDeclaration();

    public boolean isStandAlone();
    public String getReadMethod();
    public String getTimeDate();
    public String getReturnType(String functionName);
    public List<FileObject> getFiles() ;


}
