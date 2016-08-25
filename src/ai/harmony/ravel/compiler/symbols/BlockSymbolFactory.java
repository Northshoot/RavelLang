package ai.harmony.ravel.compiler.symbols;

import ai.harmony.ravel.antlr4.RavelParser;
import ai.harmony.ravel.compiler.exceptions.NoSuchBlockSymbolException;
import ai.harmony.ravel.compiler.exceptions.SymbolNotAllowedInScopeException;
import ai.harmony.ravel.compiler.scopes.Scope;

/**
 * Created by lauril on 8/19/16.
 */
public class BlockSymbolFactory {

    public static BlockSymbol getBlockSymbol(int type, String name, Scope currentScope)
                                                        throws NoSuchBlockSymbolException,
                                                               SymbolNotAllowedInScopeException

    {
        switch (type){
//            case RavelParser.CONTROLLERS:
//                return new SpaceControllerSymbol(name, currentScope);
//            case RavelParser.MODELS:
//                return new SpaceModelsSymbol(name, currentScope);
//            case RavelParser.PROPERTIES:
//                return new ModelPropertySymbol(name, currentScope);
//            case RavelParser.SCHEMA:
//                return new SchemaSymbol(name, currentScope);
//            case RavelParser.SINKS:
//                return new SinkSymbol(name, currentScope);
//            case RavelParser.SOURCES:
//                return new SourceSymbol(name, currentScope);
//            case RavelParser.PLATFORM:
//                return new SpacePlatformSymbol(name, currentScope);
            default:
                throw new NoSuchBlockSymbolException("Block symbol >>" + name +   "<< does not exist");

        }

    }
}
