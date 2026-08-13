package client;

import java.net.URI;

/** Transport seam used by the Data Dragon client and by deterministic tests. */
@FunctionalInterface
public interface DataDragonTransport {
    byte[] get(URI uri);
}
